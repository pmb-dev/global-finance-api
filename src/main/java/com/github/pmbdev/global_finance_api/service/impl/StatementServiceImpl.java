package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.exception.custom.AccountNotFoundException;
import com.github.pmbdev.global_finance_api.exception.custom.UnauthorizedAccountAccessException;
import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.TransactionRepository;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private String formatAmount(BigDecimal amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        return df.format(amount);
    }

    public byte[] generateAccountStatement(String accountNumber) {

        // Check if the account exists in the database
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the account is from the logged user
        if (!account.getUser().getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedAccountAccessException("Account " + accountNumber + " not owned by sender.");
        }

        List<TransactionEntity> transactions = transactionRepository.findBySenderOrReceiver(account, account);
        transactions.sort(Comparator.comparing(TransactionEntity::getTimestamp).reversed());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        // Colors & Fonts
        Color blueDark = new Color(30, 58, 138);
        Color tealMain = new Color(20, 184, 166);
        Color softGray = new Color(243, 244, 246);
        Font whiteTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.WHITE);
        Font whiteSub = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(219, 234, 254));
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(17, 24, 39));
        Font blackBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(17, 24, 39));
        Font grayLabel = FontFactory.getFont(FontFactory.HELVETICA, 9, new Color(75, 85, 99));

        // --- 1. HEADER ---
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1.2f, 5.8f, 3f});

        PdfPCell logoCell = new PdfPCell();
        logoCell.setPaddingLeft(35);
        logoCell.setPaddingTop(58);
        logoCell.setPaddingBottom(20);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        logoCell.setCellEvent(new GradientCellEvent(blueDark, tealMain, false));
        try {
            Image logo = Image.getInstance(getClass().getResource("/static/images/logo.png"));
            logo.scaleToFit(45, 45);
            logoCell.addElement(logo);
        } catch (Exception e) {}
        header.addCell(logoCell);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setPaddingTop(48);
        titleCell.setPaddingBottom(30);
        titleCell.setPaddingLeft(20);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setCellEvent(new GradientCellEvent(blueDark, tealMain, false));
        titleCell.addElement(new Paragraph("Global Finance API", whiteTitle));
        titleCell.addElement(new Paragraph("Bank Extract", whiteSub));
        header.addCell(titleCell);

        PdfPCell dateCell = new PdfPCell();
        dateCell.setCellEvent(new GradientCellEvent(blueDark, tealMain, false));
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dateCell.setPaddingRight(40);
        dateCell.setPaddingTop(40);
        dateCell.setPaddingBottom(30);

        Paragraph pDateLabel = new Paragraph("Extract Date", whiteSub);
        pDateLabel.setAlignment(Element.ALIGN_RIGHT);
        dateCell.addElement(pDateLabel);
        Paragraph pDateVal = new Paragraph(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.WHITE));
        pDateVal.setAlignment(Element.ALIGN_RIGHT);
        dateCell.addElement(pDateVal);
        header.addCell(dateCell);
        document.add(header);

        // --- 2. ACCOUNT INFO ---
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100);

        PdfPCell holderCell = new PdfPCell();
        holderCell.setPadding(25);
        holderCell.setBackgroundColor(softGray);
        holderCell.setBorder(Rectangle.BOTTOM);
        holderCell.setBorderColorBottom(blueDark);
        holderCell.setBorderWidthBottom(3f);
        holderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        holderCell.addElement(new Paragraph("Account holder name", grayLabel));
        holderCell.addElement(new Paragraph(account.getUser().getName(), nameFont));
        info.addCell(holderCell);

        PdfPCell balanceCell = new PdfPCell();
        balanceCell.setPadding(25);
        balanceCell.setBackgroundColor(softGray);
        balanceCell.setBorder(Rectangle.BOTTOM);
        balanceCell.setBorderColorBottom(blueDark);
        balanceCell.setBorderWidthBottom(3f);

        Paragraph pSalLabel = new Paragraph("Current Balance", grayLabel);
        pSalLabel.setAlignment(Element.ALIGN_RIGHT);
        balanceCell.addElement(pSalLabel);

        String formattedBalance = formatAmount(account.getBalance()) + " " + account.getCurrency();
        Paragraph pAmt = new Paragraph(formattedBalance,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, blueDark));
        pAmt.setAlignment(Element.ALIGN_RIGHT);
        balanceCell.addElement(pAmt);

        info.addCell(balanceCell);
        document.add(info);

        // --- 3. TRANSACTIONS ---
        float tableWidth = 92f;

        PdfPTable sectionHeader = new PdfPTable(2);
        sectionHeader.setWidthPercentage(tableWidth);
        sectionHeader.setWidths(new float[]{1f, 1f});
        sectionHeader.setSpacingBefore(14f);
        sectionHeader.setSpacingAfter(4f);

        PdfPCell thLeft = new PdfPCell(new Paragraph("Transaction History",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(31, 41, 55))));
        thLeft.setBorder(Rectangle.NO_BORDER);
        thLeft.setHorizontalAlignment(Element.ALIGN_LEFT);
        thLeft.setVerticalAlignment(Element.ALIGN_BOTTOM);
        thLeft.setPadding(0f);
        sectionHeader.addCell(thLeft);

        PdfPCell thRight = new PdfPCell(new Paragraph(account.getAccountNumber(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(75, 85, 99))));
        thRight.setBorder(Rectangle.NO_BORDER);
        thRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
        thRight.setVerticalAlignment(Element.ALIGN_BOTTOM);
        thRight.setPadding(0f);
        sectionHeader.addCell(thRight);

        document.add(sectionHeader);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(tableWidth);
        table.setWidths(new float[]{15, 35, 15, 15, 20});
        table.setSpacingBefore(6f);

        String[] hTexts = {"Date", "Description", "Expenses", "Income", "Balance"};
        for (String h : hTexts) {
            PdfPCell c = new PdfPCell(new Paragraph(h, blackBold));
            c.setBackgroundColor(new Color(224, 242, 254));
            c.setPadding(10);
            c.setBorder(Rectangle.BOTTOM);
            c.setBorderColorBottom(tealMain);
            table.addCell(c);
        }

        BigDecimal currentRunningBalance = account.getBalance();
        for (TransactionEntity tx : transactions) {
            table.addCell(createCell(tx.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), false));
            table.addCell(createCell(tx.getCategory() + " - " + tx.getConcept(), false));

            boolean isExpense = tx.getSender().getAccountNumber().equals(account.getAccountNumber());

            BigDecimal displayAmt = isExpense ? tx.getAmount() : tx.getTargetAmount();

            table.addCell(createAmountCell(isExpense ? "-" + formatAmount(displayAmt) : "", new Color(220, 38, 38), true));
            table.addCell(createAmountCell(!isExpense ? "+" + formatAmount(displayAmt) : "", new Color(22, 163, 74), true));
            table.addCell(createAmountCell(
                    formatAmount(currentRunningBalance) + " " + account.getCurrency(),
                    new Color(75, 85, 99),
                    true));

            if (isExpense) {
                currentRunningBalance = currentRunningBalance.add(displayAmt);
            } else {
                currentRunningBalance = currentRunningBalance.subtract(displayAmt);
            }
        }

        document.add(table);

        // Footer
        PdfPTable footer = new PdfPTable(1);
        footer.setWidthPercentage(100);
        footer.setSpacingBefore(30);
        PdfPCell fCell = new PdfPCell(
                new Paragraph("© 2026 Global Finance API | Electronically generated document", grayLabel));
        fCell.setBorder(Rectangle.TOP);
        fCell.setBorderColorTop(new Color(229, 231, 235));
        fCell.setPadding(20);
        fCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footer.addCell(fCell);

        document.add(footer);
        document.close();
        return out.toByteArray();
    }

    private PdfPCell createCell(String text, boolean bold) {
        PdfPCell cell = new PdfPCell(new Paragraph(text,
                FontFactory.getFont(bold ? FontFactory.HELVETICA_BOLD : FontFactory.HELVETICA, 10)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(10);
        return cell;
    }

    private PdfPCell createAmountCell(String text, Color color, boolean alignRight) {
        PdfPCell cell = new PdfPCell(new Paragraph(text,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, color)));
        cell.setBorder(Rectangle.NO_BORDER);
        if (alignRight) cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(10);
        return cell;
    }
}

class GradientCellEvent implements PdfPCellEvent {
    protected Color color1;
    protected Color color2;
    protected boolean horizontal;

    public GradientCellEvent(Color color1, Color color2, boolean horizontal) {
        this.color1 = color1;
        this.color2 = color2;
        this.horizontal = horizontal;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
        PdfShading shading = PdfShading.simpleAxial(cb.getPdfWriter(),
                position.getLeft(), position.getBottom(),
                horizontal ? position.getRight() : position.getLeft(),
                horizontal ? position.getBottom() : position.getTop(),
                color1, color2);
        PdfShadingPattern pattern = new PdfShadingPattern(shading);
        cb.setShadingFill(pattern);
        cb.rectangle(position.getLeft(), position.getBottom(), position.getWidth(), position.getHeight());
        cb.fill();
    }
}
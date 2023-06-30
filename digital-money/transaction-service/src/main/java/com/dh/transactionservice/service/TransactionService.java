package com.dh.transactionservice.service;

import com.dh.transactionservice.entities.Transaction;
import com.dh.transactionservice.exceptions.BadRequestException;
import com.dh.transactionservice.exceptions.ResourceNotFountException;
import com.dh.transactionservice.repository.ITransactionRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    ITransactionRepository repository;
    public Optional<List<Transaction>> listLastTransactionsByAccountId(Long id) throws ResourceNotFountException {
       Optional<List<Transaction>>  transactions = repository.findLatestTransactionsByAccountId(id);
       if(transactions.get().isEmpty()){
           throw new ResourceNotFountException("We did not find any transactions related to the account ID.");
       }else{
           return transactions;
       }
    }
    public Optional<List<Transaction>> transactionsByAccountId(Long id) throws ResourceNotFountException {
        Optional<List<Transaction>>  transactions =repository.findTransactionsByAccountId(id);
        if(transactions.get().isEmpty()){
            throw new ResourceNotFountException("We did not find any transactions related to the account ID.");
        }else{
            return transactions;
        }
    }
    public Optional<List<Transaction>> transactionsByAccountIdAndRange(Long id,Double rangeA,Double rangeB ) throws ResourceNotFountException {
        Optional<List<Transaction>>  transactions = repository.findTransactionsByAccountIdAndAmountRange(id, rangeA, rangeB);
        if(transactions.get().isEmpty()){
            throw new ResourceNotFountException("We did not find any transactions related to the account ID.");
        }else{
            return transactions;
        }
    }
    public Transaction createTransaction (Transaction transaction) throws BadRequestException {
        if (!isValidCvuLength(transaction.getDestination_cvu())){
            throw new BadRequestException("We cannot create the transaction because the length of the destination_cvu is not correct");
        } else if (!validNumericCvu(transaction.getDestination_cvu())) {
           throw new BadRequestException("Invalid CVU format. The destination_cvu must contain only numeric characters.");
        }
        if (!isValidCvuLength(transaction.getOrigin_cvu())){
            throw new BadRequestException("We cannot create the user account because the length of the origin_cvu is not correct");
        } else if (!validNumericCvu(transaction.getOrigin_cvu())) {
            throw new BadRequestException("Invalid CVU format. The origin_cvu must contain only numeric characters.");
        }
        transaction.setDate(LocalDateTime.now());
        return repository.save(transaction);
    }
    public Transaction getTransactionById ( Long id) throws ResourceNotFountException {
        Optional<Transaction> transactionSearched = repository.findById(id);
        if (transactionSearched.isPresent()){
            return  transactionSearched.get();
        }else {
            throw new ResourceNotFountException("We can get the information because is a non-existence transaction");
        }
    }
    public byte[] transactionReceipt (Long id) throws BadRequestException {
        Optional<Transaction> searchedTransaction = repository.findById(id);
        if (searchedTransaction.isPresent()){
        Transaction transaction = searchedTransaction.get();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            ClassLoader classLoader = getClass().getClassLoader();
            URL resourceUrl = classLoader.getResource("logo.png");
            String imageUrl = resourceUrl.getPath();
            Image logo = Image.getInstance(imageUrl);
            logo.scaleToFit(150,150);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            Font titleFont= FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);
            Paragraph title = new Paragraph("Transaction receipt generated from the digital-money app :",titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            Paragraph date =new Paragraph("Date : "+new Date().toString());
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(60);
            document.add(date);
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
                titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);
            PdfPCell cell = new PdfPCell(new Paragraph("TRANSACTION INFORMATION",titleFont));
            cell.setColspan(2);
            cell.setHorizontalAlignment (Element.ALIGN_CENTER);
            cell.setPadding (10.0f);
            cell.setBackgroundColor (new BaseColor(140, 221, 8));
            table.addCell(cell);

            Field[] fields = transaction.getClass().getDeclaredFields();
            titleFont = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
            for (Field field : fields){
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(transaction);

                PdfPCell nameCell = new PdfPCell(new Paragraph(fieldName.toUpperCase(),titleFont));
                table.addCell(fieldName);
                table.addCell(String.valueOf(fieldValue));
        }
            document.add(table);
            document.close();
            return outputStream.toByteArray();
            } catch (DocumentException e) {
            throw new BadRequestException("Error creating PDF document: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new BadRequestException("Error accessing transaction properties: " + e.getMessage());
        } catch (IOException e) {
            throw new BadRequestException("Error in the input or output of the document "+e.getMessage());
        }

        }else{
            throw new BadRequestException("You´re trying to generate a receipt of a non-existence transaction");
        }
    }


    private boolean isValidCvuLength(String cvu){
        return cvu.length() == 22;
    }
    private boolean validNumericCvu(String cvu){
        return cvu.matches("\\d+");
    }
}

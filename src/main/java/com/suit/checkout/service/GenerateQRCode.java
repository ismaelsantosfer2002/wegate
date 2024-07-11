package com.suit.checkout.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class GenerateQRCode {

    public String generateQRCodeBase64(String pixCode) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 350;
        int height = 350;
        BitMatrix bitMatrix = qrCodeWriter.encode(pixCode, BarcodeFormat.QR_CODE, width, height);

        // Write to a BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Convert BufferedImage to ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Encode ByteArrayOutputStream to Base64
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return base64Image;
    }

}

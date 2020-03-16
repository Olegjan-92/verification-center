package com.certification.verificationcenter;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
class VerificationCenterApplicationTests {

    @Test
    void contextLoads() throws Exception{

        BouncyCastleProvider bcProvider = new BouncyCastleProvider();
        String name = bcProvider.getName();
        bcProvider.addKeyInfoConverter(new ASN1ObjectIdentifier("1.2.643.7.1.1.2.3"), new AsymmetricKeyInfoConverter() {
            @Override
            public PrivateKey generatePrivate(PrivateKeyInfo keyInfo) throws IOException {
                return null;
            }

            @Override
            public PublicKey generatePublic(SubjectPublicKeyInfo keyInfo) throws IOException {
                return null;
            }
        });
        Security.removeProvider(name);
        Security.addProvider(bcProvider);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECGOST3410-2012", "BC");
        keyPairGenerator.initialize(new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA"));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

//        X500Name subject = new X500Name("CN=Me INN=123456");

        X500NameBuilder builder = new X500NameBuilder(RFCManual.INSTANCE);

        builder.addRDN(RFCManual.c, "RU");
        builder.addRDN(RFCManual.o, "HCFB");
        builder.addRDN(RFCManual.l, "BELGOROD");
        builder.addRDN(RFCManual.st, "BELGOROD1");
        builder.addRDN(RFCManual.ogrn, "BELGOROD1");
//        builder.addRDN(new ASN1ObjectIdentifier("1.2.643.7.1.1.2.3"), "123123");

        X500Name subject = builder.build();


        BigInteger serial = BigInteger.ONE; // serial number for self-signed does not matter a lot
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + TimeUnit.DAYS.toMillis(365));

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(subject, serial, notBefore, notAfter, subject, keyPair.getPublic());
        X509CertificateHolder certificateHolder = certificateBuilder.build(new JcaContentSignerBuilder("GOST3411WITHECGOST3410-2012-512").build(keyPair.getPrivate()));
        JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter();

        X509Certificate certificate = certificateConverter.getCertificate(certificateHolder);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null); // initialize new keystore
        keyStore.setEntry("alias", new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new X509Certificate[]{certificate}),
                new KeyStore.PasswordProtection("entryPassword".toCharArray()));
        keyStore.store(new FileOutputStream("test.jks"), "keystorePassword".toCharArray());
    }

}

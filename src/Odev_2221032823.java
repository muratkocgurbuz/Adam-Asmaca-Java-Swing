import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Odev_2221032823 {

    String yolTxt = "C:\\P2Oyun\\TXTDosyalar\\";
    String yolResim = "C:\\P2Oyun\\Resimler\\";

    JFrame anaCerceve;
    JTabbedPane sekmePaneli;
    JPanel pnlOyun, pnlSkor, pnlLog;
    JMenuBar menuCubugu;
    JMenu menuSecenekler;
    JMenuItem mnBasla, mnYeniden, mnCikis;

    JPanel pnlHarfler;
    JLabel lblResim, lblSure;
    JTextField txtHarf, txtKelime;
    JButton btnHarfTahmin, btnKelimeTahmin;
    Timer oyunZamanlayici;
    int saniye, hataSayisi;
    String secilenKelime;
    ArrayList<JLabel> gizliHarflerListesi;
    boolean oyunAktifMi;

    JTable tabloSkor, tabloLog;
    DefaultTableModel modelSkor, modelLog;
    JButton btnSkorTemizle, btnLogTemizle;

    public Odev_2221032823() {
        if (sifreKontrolEt()) {
            logYaz("Sisteme başarılı giriş yapıldı.");
            arayuzuKur();
        } else {
            System.exit(0);
        }
    }

    private boolean sifreKontrolEt() {
        File sifreDosyasi = new File(yolTxt + "sifre.txt");
        String kayitliSifre = "";

        try {
            Scanner okuyucu = new Scanner(sifreDosyasi);
            if (okuyucu.hasNext()) {
                kayitliSifre = okuyucu.nextLine();
            }
            okuyucu.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "sifre.txt dosyası bulunamadı!");
            return false;
        }

        if (kayitliSifre.isEmpty()) {
            String yeniSifre = JOptionPane.showInputDialog(null, "Lütfen yeni bir şifre belirleyin:", "Şifre Belirleme", JOptionPane.INFORMATION_MESSAGE);
            if (yeniSifre != null && !yeniSifre.trim().isEmpty()) {
                try {
                    FileWriter fw = new FileWriter(sifreDosyasi);
                    fw.write(yeniSifre.trim());
                    fw.close();
                    JOptionPane.showMessageDialog(null, "Şifre başarıyla kaydedildi. Şimdi giriş yapabilirsiniz.");
                    kayitliSifre = yeniSifre.trim();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Şifre kaydedilemedi!");
                    return false;
                }
            } else {
                return false;
            }
        }

        int denemeHakki = 3;
        while (denemeHakki > 0) {
            String girilenSifre = JOptionPane.showInputDialog(null, "Şifrenizi Giriniz (Kalan Hak: " + denemeHakki + "):", "Giriş", JOptionPane.QUESTION_MESSAGE);
            if (girilenSifre == null) {
                logYaz("Giriş iptal edildi.");
                return false;
            }
            if (girilenSifre.equals(kayitliSifre)) {
                return true;
            } else {
                denemeHakki--;
                logYaz("Hatalı şifre denemesi.");
                JOptionPane.showMessageDialog(null, "Hatalı şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
        logYaz("3 kez hatalı giriş yapıldı, program sonlandı.");
        JOptionPane.showMessageDialog(null, "3 kez hatalı giriş yaptınız. Program kapanıyor.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private void arayuzuKur() {
        anaCerceve = new JFrame("Adam Asmaca");
        anaCerceve.setSize(800, 600);
        anaCerceve.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        anaCerceve.setLayout(new BorderLayout());

        menuCubugu = new JMenuBar();
        menuSecenekler = new JMenu("Oyuna Başlama Seçenekleri");
        mnBasla = new JMenuItem("Oyuna Başla");
        mnYeniden = new JMenuItem("Oyunu Yeniden Başlat");
        mnCikis = new JMenuItem("Çıkış");

        menuSecenekler.add(mnBasla);
        menuSecenekler.add(mnYeniden);
        menuSecenekler.addSeparator();
        menuSecenekler.add(mnCikis);
        menuCubugu.add(Box.createHorizontalGlue());
        menuCubugu.add(menuSecenekler);
        anaCerceve.setJMenuBar(menuCubugu);

        sekmePaneli = new JTabbedPane();
        pnlOyun = new JPanel(new BorderLayout());
        pnlSkor = new JPanel(new BorderLayout());
        pnlLog = new JPanel(new BorderLayout());

        sekmePaneli.addTab("Oyun Oynama", pnlOyun);
        sekmePaneli.addTab("Eski Skorları Görüntüleme", pnlSkor);
        sekmePaneli.addTab("Logları Görüntüleme", pnlLog);

        anaCerceve.add(sekmePaneli, BorderLayout.CENTER);

        oyunPaneliniDoldur();
        skorPaneliniDoldur();
        logPaneliniDoldur();

        mnBasla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oyunuBaslat();
                sekmePaneli.setSelectedIndex(0);
            }
        });

        mnYeniden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (oyunAktifMi) {
                    oyunZamanlayici.stop();
                }
                oyunuBaslat();
                sekmePaneli.setSelectedIndex(0);
            }
        });

        mnCikis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        tablolariGuncelle();
        anaCerceve.setVisible(true);
    }

    private void oyunPaneliniDoldur() {
        JPanel pnlUst = new JPanel(new BorderLayout());
        lblSure = new JLabel("Süre: 0 saniye", SwingConstants.CENTER);
        lblSure.setFont(new Font("Arial", Font.BOLD, 16));
        pnlUst.add(lblSure, BorderLayout.NORTH);

        lblResim = new JLabel(new ImageIcon(yolResim + "1.jpg"), SwingConstants.CENTER);
        pnlUst.add(lblResim, BorderLayout.CENTER);
        pnlOyun.add(pnlUst, BorderLayout.NORTH);

        pnlHarfler = new JPanel(new FlowLayout());
        pnlOyun.add(pnlHarfler, BorderLayout.CENTER);

        JPanel pnlAlt = new JPanel(new GridLayout(2, 3, 10, 10));
        pnlAlt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtHarf = new JTextField();
        btnHarfTahmin = new JButton("Harf Tahmin Et");
        pnlAlt.add(new JLabel("Harf Girin:", SwingConstants.RIGHT));
        pnlAlt.add(txtHarf);
        pnlAlt.add(btnHarfTahmin);

        txtKelime = new JTextField();
        btnKelimeTahmin = new JButton("Kelime Tahmin Et");
        pnlAlt.add(new JLabel("Kelime Girin:", SwingConstants.RIGHT));
        pnlAlt.add(txtKelime);
        pnlAlt.add(btnKelimeTahmin);

        pnlOyun.add(pnlAlt, BorderLayout.SOUTH);

        oyunZamanlayici = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saniye++;
                lblSure.setText("Süre: " + saniye + " saniye");
            }
        });

        btnHarfTahmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!oyunAktifMi) return;
                
                String tahmin = txtHarf.getText().toLowerCase().trim();
                txtHarf.setText("");
                
                if (tahmin.length() != 1) {
                    JOptionPane.showMessageDialog(anaCerceve, "Lütfen sadece 1 harf giriniz.");
                    return;
                }

                char harf = tahmin.charAt(0);
                boolean dogruMu = false;

                for (int i = 0; i < secilenKelime.length(); i++) {
                    if (secilenKelime.charAt(i) == harf) {
                        gizliHarflerListesi.get(i).setText(String.valueOf(harf));
                        dogruMu = true;
                    }
                }

                if (!dogruMu) {
                    hataliTahminIsle();
                }
                kazanmaDurumunuKontrolEt();
            }
        });

        btnKelimeTahmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!oyunAktifMi) return;

                String tahmin = txtKelime.getText().toLowerCase().trim();
                txtKelime.setText("");

                if (tahmin.equals(secilenKelime)) {
                    for (int i = 0; i < secilenKelime.length(); i++) {
                        gizliHarflerListesi.get(i).setText(String.valueOf(secilenKelime.charAt(i)));
                    }
                    oyunuBitir(true);
                } else {
                    hataliTahminIsle();
                }
            }
        });
        
        btnHarfTahmin.setEnabled(false);
        btnKelimeTahmin.setEnabled(false);
    }

    private void skorPaneliniDoldur() {
        modelSkor = new DefaultTableModel(new String[]{"Tarih/Saat", "Süre (sn)", "Sonuç"}, 0);
        tabloSkor = new JTable(modelSkor);
        pnlSkor.add(new JScrollPane(tabloSkor), BorderLayout.CENTER);

        JPanel pnlSkorAlt = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSkorTemizle = new JButton("Temizle");
        pnlSkorAlt.add(btnSkorTemizle);
        pnlSkor.add(pnlSkorAlt, BorderLayout.SOUTH);

        btnSkorTemizle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dosyaTemizleIslemi(yolTxt + "oyunlar.txt", modelSkor);
            }
        });
    }

    private void logPaneliniDoldur() {
        modelLog = new DefaultTableModel(new String[]{"Log Tarih/Saat", "Açıklama"}, 0);
        tabloLog = new JTable(modelLog);
        pnlLog.add(new JScrollPane(tabloLog), BorderLayout.CENTER);

        JPanel pnlLogAlt = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLogTemizle = new JButton("Temizle");
        pnlLogAlt.add(btnLogTemizle);
        pnlLog.add(pnlLogAlt, BorderLayout.SOUTH);

        btnLogTemizle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dosyaTemizleIslemi(yolTxt + "log.txt", modelLog);
            }
        });
    }

    private void oyunuBaslat() {
        ArrayList<String> kelimeler = new ArrayList<>();
        try {
            Scanner okuyucu = new Scanner(new File(yolTxt + "kelimeler.txt"));
            while (okuyucu.hasNext()) {
                kelimeler.add(okuyucu.next().toLowerCase());
            }
            okuyucu.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(anaCerceve, "kelimeler.txt bulunamadı!");
            return;
        }

        if (kelimeler.isEmpty()) {
            JOptionPane.showMessageDialog(anaCerceve, "Kelimeler dosyası boş!");
            return;
        }

        Random rnd = new Random();
        secilenKelime = kelimeler.get(rnd.nextInt(kelimeler.size()));

        hataSayisi = 0;
        saniye = 0;
        lblSure.setText("Süre: 0 saniye");
        lblResim.setIcon(new ImageIcon(yolResim + "1.jpg"));
        pnlHarfler.removeAll();
        gizliHarflerListesi = new ArrayList<>();

        for (int i = 0; i < secilenKelime.length(); i++) {
            JLabel lblHarf = new JLabel("*");
            lblHarf.setFont(new Font("Arial", Font.BOLD, 24));
            lblHarf.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            lblHarf.setPreferredSize(new Dimension(30, 40));
            lblHarf.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHarfler.add(lblHarf);
            gizliHarflerListesi.add(lblHarf);
        }

        pnlHarfler.revalidate();
        pnlHarfler.repaint();

        btnHarfTahmin.setEnabled(true);
        btnKelimeTahmin.setEnabled(true);
        oyunAktifMi = true;
        oyunZamanlayici.start();
        
        logYaz("Yeni oyun başlatıldı.");
    }

    private void hataliTahminIsle() {
        hataSayisi++;
        int gosterilecekResimNo = hataSayisi + 1;
        if (gosterilecekResimNo <= 11) {
            lblResim.setIcon(new ImageIcon(yolResim + gosterilecekResimNo + ".jpg"));
        } else {
            lblResim.setIcon(new ImageIcon(yolResim + "11.jpg"));
        }

        if (hataSayisi >= 11) {
            oyunuBitir(false);
        }
    }

    private void kazanmaDurumunuKontrolEt() {
        boolean kazandiMi = true;
        for (JLabel lbl : gizliHarflerListesi) {
            if (lbl.getText().equals("*")) {
                kazandiMi = false;
                break;
            }
        }
        if (kazandiMi) {
            oyunuBitir(true);
        }
    }

    private void oyunuBitir(boolean kazandiMi) {
        oyunZamanlayici.stop();
        oyunAktifMi = false;
        btnHarfTahmin.setEnabled(false);
        btnKelimeTahmin.setEnabled(false);

        String sonuc = kazandiMi ? "Kazandı" : "Kaybetti";
        String suankiZaman = zamanGetir();
        
        try {
            FileWriter fw = new FileWriter(yolTxt + "oyunlar.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(suankiZaman + ";" + saniye + ";" + sonuc);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            System.out.println("Skor kaydedilemedi.");
        }

        logYaz("Oyun bitti. Sonuç: " + sonuc + ", Süre: " + saniye);
        tablolariGuncelle();

        if (kazandiMi) {
            JOptionPane.showMessageDialog(anaCerceve, "Tebrikler! Kelimeyi bildiniz: " + secilenKelime + "\nSüreniz: " + saniye + " saniye", "Kazandınız", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(anaCerceve, "Maalesef asıldınız! Kelime: " + secilenKelime, "Kaybettiniz", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logYaz(String islem) {
        try {
            FileWriter fw = new FileWriter(yolTxt + "log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(zamanGetir() + ";" + islem);
            bw.newLine();
            bw.close();
            tablolariGuncelle();
        } catch (IOException ex) {
            System.out.println("Log yazılamadı.");
        }
    }

    private void dosyaTemizleIslemi(String dosyaYolu, DefaultTableModel model) {
        String girilen = JOptionPane.showInputDialog(anaCerceve, "Temizlemek için şifrenizi giriniz:");
        if (girilen == null) return;

        String kayitliSifre = "";
        try {
            Scanner okuyucu = new Scanner(new File(yolTxt + "sifre.txt"));
            if (okuyucu.hasNext()) kayitliSifre = okuyucu.nextLine();
            okuyucu.close();
        } catch (Exception ex) {
            return;
        }

        if (girilen.equals(kayitliSifre)) {
            try {
                FileWriter fw = new FileWriter(dosyaYolu, false);
                fw.write("");
                fw.close();
                if (model != null) {
                    model.setRowCount(0);
                }
                logYaz("Tablo verileri temizlendi: " + new File(dosyaYolu).getName());
                JOptionPane.showMessageDialog(anaCerceve, "Veriler başarıyla temizlendi.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(anaCerceve, "Temizleme hatası!");
            }
        } else {
            logYaz("Temizleme için hatalı şifre denemesi.");
            JOptionPane.showMessageDialog(anaCerceve, "Hatalı şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tablolariGuncelle() {
        if (modelSkor != null) {
            modelSkor.setRowCount(0);
            try {
                Scanner okuyucu = new Scanner(new File(yolTxt + "oyunlar.txt"));
                while (okuyucu.hasNextLine()) {
                    String satir = okuyucu.nextLine();
                    String[] parcalar = satir.split(";");
                    if (parcalar.length == 3) {
                        modelSkor.addRow(parcalar);
                    }
                }
                okuyucu.close();
            } catch (Exception e) {}
        }

        if (modelLog != null) {
            modelLog.setRowCount(0);
            try {
                Scanner okuyucu = new Scanner(new File(yolTxt + "log.txt"));
                while (okuyucu.hasNextLine()) {
                    String satir = okuyucu.nextLine();
                    String[] parcalar = satir.split(";");
                    if (parcalar.length == 2) {
                        modelLog.addRow(parcalar);
                    }
                }
                okuyucu.close();
            } catch (Exception e) {}
        }
    }

    private String zamanGetir() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime simdi = LocalDateTime.now();
        return dtf.format(simdi);
    }

    public static void main(String[] args) {
        new Odev_2221032823();
    }
}
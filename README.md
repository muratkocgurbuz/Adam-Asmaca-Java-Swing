Markdown
# 🎮 Adam Asmaca (Hangman) Oyunu - Java Swing

Bu proje, Süleyman Demirel Üniversitesi Programlama 2 dersi kapsamında geliştirilmiş, Java Swing arayüzü kullanan kapsamlı bir Adam Asmaca oyunudur.

## 🌟 Projenin Amacı ve Özellikleri

Geleneksel Adam Asmaca oyununun dijital bir versiyonu olan bu projede, kullanıcı deneyimi ve veri güvenliği ön planda tutulmuştur.

* **🔐 Güvenli Giriş Sistemi:** Uygulama açılışında kullanıcıdan şifre istenir. İlk girişte şifre belirlenir ve sistem tarafından `sifre.txt` dosyasına kaydedilir. Hatalı girişlerde (maksimum 3 hak) program güvenlik amacıyla sonlanır.
* **🕹️ Dinamik Oyun Paneli:** Rastgele seçilen kelimeler için harf sayısı kadar dinamik etiket (`JLabel`) oluşturulur.
* **⌨️ Çift Yönlü Tahmin:** Kullanıcılar hem tekil harf tahmini hem de doğrudan kelime tahmini yapabilirler. Yanlış tahminlerde darağacı görseli adım adım güncellenir. Tam 11 hatada oyun sonlanır.
* **📊 Log ve Skor Takibi:** Oyuna yapılan girişler, hatalı şifre denemeleri ve oyun sonuçları (süre ve tarih bilgisiyle) `log.txt` ve `oyunlar.txt` dosyalarına kaydedilir.
* **🧹 Tablo Yönetimi:** Kullanıcılar arayüzdeki JTable üzerinden geçmiş skorları ve log kayıtlarını inceleyebilir. Ayrıca şifre doğrulama ile bu kayıtları güvenle temizleyebilirler.

## 🛠️ Kullanılan Teknolojiler

* **Programlama Dili:** Java
* **Arayüz (GUI):** Java Swing (`JFrame`, `JTabbedPane`, `JMenuBar`, `JPanel`, `JTable`, `Timer`)
* **Dosya İşlemleri (I/O):** `File`, `Scanner`, `FileWriter`, `BufferedWriter` (Geleneksel yöntemler)

## 📁 Dosya ve Klasör Yapısı

Projenin sorunsuz çalışabilmesi için bilgisayarın `C:\` sürücüsünde aşağıdaki dizin yapısının bulunması zorunludur:

```text
C:\P2Oyun
├── Resimler
│   ├── 1.jpg
│   ├── ...
│   └── 11.jpg
└── TXTDosyalar
    ├── kelimeler.txt
    ├── log.txt
    ├── oyunlar.txt
    └── sifre.txt


🚀 Nasıl Çalıştırılır?
1-Repoyu bilgisayarınıza indirin veya klonlayın.

2-P2Oyun klasörünü bilgisayarınızın doğrudan C:\ dizinine kopyalayın.

3-Projeyi tercih ettiğiniz bir IDE (Örn: NetBeans) ile açıp Odev_2221032823.java dosyasını çalıştırın.

Geliştirici: Murat Koçgürbüz

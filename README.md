# JavaProjectPOS - Satış Noktası Sistemi

Perakende operasyonlarını yönetmek için ürün envanteri, kullanıcı kimlik doğrulaması ve satış işlemleri içeren Java Swing ile oluşturulmuş masaüstü tabanlı bir Satış Noktası sistemi.

## İçindekiler
- [Kurulum Talimatları](#kurulum-talimatları)
- [Veritabanı Şeması](#veritabanı-şeması)
- [Proje Yapısı](#proje-yapısı)
- [Java Dosyaları Belgelendirmesi](#java-dosyaları-belgelendirmesi)
- [Veritabanı Kurulumu](#veritabanı-kurulumu)

## Kurulum Talimatları

1. Eclipse Marketplace'ten Window Builder'ı indirin ve kurun
2. Proje kök dizini altında bir `Data` alt dizini oluşturun.
3. `rs2xml.jar`, `sqlite.jar` ve `sqlitejdbc.jar` dosyalarını Data dizinine taşıyın.
4. Google Chrome'u açın ve `SQLite Manager` uzantısını Google Chrome Marketplace'ten kurun.
5. Chrome'da yeni uzantıyı kullanarak yeni bir veritabanı oluşturun ve indirin.
6. İndirdiğiniz dosyayı Data alt dizininize taşıyın.
7. Eclipse'te kök dizininizi sağ tıklayın. `Build Path` -> `Configure Build Path...` seçeneğine gidin
8. Java Build Path bölümüne gidin ve Libraries'i tıklayın, Modulepath'i tıklayın. `Add JARs...` seçeneğini tıklayın ve `sqlite.jar` ile `sqlitejdbc.jar` dosyalarını seçin, Tamam'ı tıklayın. Şimdi Classpath'i tıklayın, `Add JARs...` seçeneğini tıklayın ve `rs2xml.jar` dosyasını seçin, Tamam'ı tıklayın. `Apply and Close` seçeneğini tıklayın.
9. src dizini altında bir Package oluşturun.
10. Package'ı sağ tıkladıktan sonra, New -> Other seçeneğine gidin.
11. Window Builder -> Swing Designer bölümünde JFrame seçeneğini seçin ve Next'i tıklayın. Şimdi uygulamanızı oluşturmaya başlayabilirsiniz.
12. Daha önce yazılmış bir JFrame kodunu yeniden açmak istediğinizde, dosyayı sağ tıklayın ve Open With seçeneğini seçin ve WindowBuilder Editor'ı seçin.

## Veritabanı Şeması

### Kullanici Tablosu (Kullanıcılar)
```
HesapNo (INTEGER, PRIMARY KEY, AUTOINCREMENT)
├─ Yeni bir kullanıcı oluşturulduğunda otomatik olarak artar
├─ 1'den başlar ve her yeni kayıt için 1 artar
└─ Her kullanıcıyı benzersiz şekilde tanımlar

KullaniciAdi (VARCHAR 30) - Giriş için kullanıcı adı
AdiSoyadi (VARCHAR 30) - Kullanıcının tam adı
PinKodu (INTEGER) - Kimlik doğrulaması için PIN/şifre
Gorev (VARCHAR 30) - Kullanıcı rolü: 'admin', 'super-admin' veya 'kasiyer'
```

### Urunler Tablosu (Ürünler)
```
UrunNo (INTEGER, PRIMARY KEY, AUTOINCREMENT)
├─ Yeni bir ürün eklendiğinde otomatik olarak artar
├─ 1'den başlar ve her yeni kayıt için 1 artar
└─ Her ürünü benzersiz şekilde tanımlar

Ad (VARCHAR 30) - Ürün adı
Fiyat (DECIMAL 10,2) - Ürün fiyatı (2 ondalık basamak ile)
Stok (INTEGER) - Mevcut stok miktarı
```

**AUTOINCREMENT Nasıl Çalışır:**
- Kimlik belirtmeden bir kayıt eklediğinizde, SQLite otomatik olarak sonraki kullanılabilir numarayı atar
- sqlite_sequence tablosu her tablo için kullanılan en yüksek kimliği izler
- Bu, hiç kimlik yinelemesini engeller ve veri bütünlüğünü korur

## Proje Yapısı

```
src/pos/
├── Baglanti.java           - Veritabanı bağlantı yöneticisi
├── Giris.java              - Giriş ekranı (başlangıç noktası)
├── AdminEkran.java         - Yönetici panosu
├── SatisEkran.java         - Satış/kasiyer arayüzü
├── UrunEkle.java           - Yeni ürün ekle
├── UrunSil.java            - Ürün sil
├── StokEkle.java           - Mevcut ürüne stok ekle
├── KullaniciEkle.java      - Yeni kullanıcı/kasiyer ekle
├── KullaniciSil.java       - Kullanıcı/kasiyer sil
├── KasiyerEkran.java       - Alternatif kasiyer ekranı
└── KasiyerEkle.java        - Yeni kasiyer ekle
```

## Java Dosyaları Belgelendirmesi

### Baglanti.java
**Amaç:** Veritabanı bağlantı yardımcı programı
- **Bagla() metodu:** SQLite veritabanına bağlantı kurar
  - SQLite JDBC sürücüsünü yükler: `org.sqlite.JDBC`
  - `Data/memory.db` dosyasına bağlanır
  - Bağlantı nesnesi döndürür veya hata oluşursa null döndürür
  - Bağlantı başarısız olursa hata iletişim kutusu gösterir
- **Kullanım:** Diğer tüm ekranlar tarafından veritabanı bağlantısı almak için çağrılır

### Giris.java
**Amaç:** Giriş ekranı (uygulama başlangıç noktası)
- **Ana Ekran:** Koyu gri arka planlı 600x500 piksellik pencere
- **Giriş Alanları:**
  - Kullanıcı adı metin alanı
  - Şifre metin alanı
  - Varsayılan kimlik bilgilerini referans olarak gösterir
- **Giriş Mantığı:**
  - Kullanıcı adı ve PIN'i kullanıcı girişinden alır
  - Hazırlanmış ifadeyi çalıştırır: `SELECT * FROM Kullanici WHERE KullaniciAdi = ? AND PinKodu = ?`
  - SQL enjeksiyonundan korunmak için PreparedStatement kullanır
  - Veritabanından kullanıcı rolünü (Gorev) kontrol eder
- **Gezinti:**
  - Yönetici/Süper yönetici (cnt=1) → AdminEkran'ı kullanıcı adı ile açar
  - Kasiyer (cnt=2) → SatisEkran'ı açar
  - Geçersiz kimlik bilgileri → "Başarısız.." hata mesajı gösterir
- **Güvenlik:** Parametrelendirilmiş sorgularla PreparedStatement kullanır

### AdminEkran.java
**Amaç:** Ürün ve kullanıcıları yönetmek için yönetici panosu
- **Yapıcı:** Oturum açan yöneticinin izlenmesi için kullanıcı adı parametresini kabul eder
  - Kullanıcı adını daha sonra kullanmak için `currentUsername` alanında depolar
  - Varsayılan yapıcı parametreli yapıcıyı boş dize ile çağırır
- **Menü Düğmeleri:**
  - **Yeni Urun Ekle** → UrunEkle ekranını açar
  - **Stok Ekle** → StokEkle ekranını açar
  - **Urun Sil** → UrunSil ekranını açar
  - **Yeni Kasiyer Ekle** → KullaniciEkle ekranını açar
  - **Kasiyer Sil** → KullaniciSil'i mevcut yönetici kullanıcı adı ile açar (kendi kendini silmeyi önler)
  - **Çıkış** → Giriş ekranına döner
- **Erişim Kontrolü:** Yalnızca 'admin' veya 'super-admin' rolüne sahip kullanıcılar tarafından erişilebilir

### SatisEkran.java
**Amaç:** İşlemleri tamamlamak için Satış/Kasiyer arayüzü
- **Ürün Gösterimi:**
  - Veritabanından stok > 0 olan tüm ürünleri yükler
  - Tablo sütunları: Ürün Adı, Fiyat, Stok, Satış Miktarı
  - Yalnızca "Satış Miktarı" sütunu düzenlenebilir
- **İşlem Mantığı:**
  - **Hesapla Düğmesi:** Toplam fiyatı (fiyat × satisMiktarı) hesaplar ve etiketi günceller
  - **Kaydet Düğmesi:**
    - Toplamın > 0 olduğunu doğrular
    - Veritabanı işlemlerini kullanır (setAutoCommit(false))
    - Sipariş edilen miktarın ≤ mevcut stoka olup olmadığını kontrol eder
    - Geçerliyse: Ürün stokunu günceller ve işlemi kaydeder
    - Geçersizse: İşlemi geri alır ve hata gösterir
    - Toplam tutarı içeren onay iletişim kutusunu gösterir
- **Hata İşleme:** Yetersiz stok veya veritabanı hataları üzerine otomatik geri alma
- **Envanter Yönetimi:** Stoğu atomik olarak güncellemek için PreparedStatement kullanır

### UrunEkle.java
**Amaç:** Envantera yeni ürün ekle
- **Giriş Alanları:**
  - Ürün Adı (Ürün adı)
  - Fiyat (Ürün fiyatı) - virgülü ondalık için noktaya dönüştürür
  - Miktar (İlk stok miktarı)
- **Doğrulama:**
  - Tüm alanların doldurulup doldurulmadığını kontrol eder
  - Fiyat ve miktarın geçerli sayılar olduğunu doğrular
  - Geçersiz giriş için hata mesajları gösterir
- **Veritabanı İşlemi:**
  - INSERT ifadesi: `INSERT INTO Urunler (Ad, Fiyat, Stok) VALUES (?, ?, ?)`
  - Otomatik artan UrunNo veritabanı tarafından atanır
  - Başarılı ekleme sonrası formu temizler
- **Gezinti:** Geri düğmesi giriş ekranına döner

### UrunSil.java
**Amaç:** Envanterden ürün sil
- **Görüntü:** Tüm ürünleri gösteren JTable (UrunNo, Ad, Fiyat, Stok)
- **Silme İşlemi:**
  - Kullanıcı metin alanına ürün UrunNo'sunu girer
  - Silinmeden önce onay iletişim kutusunu gösterir
  - DELETE ifadesi: `DELETE FROM Urunler WHERE UrunNo = ?`
  - Başarılı silme sonrası tabloyu yeniler
  - Ürün numarası bulunamazsa hata gösterir
- **Hata İşleme:** Girişin sayısal olduğunu doğrular, uygun hata mesajlarını gösterir
- **Gezinti:** Geri düğmesi giriş ekranına döner

### StokEkle.java
**Amaç:** Mevcut ürünlere stok miktarı ekle
- **Görüntü:** Tüm ürünleri içeren JTable
- **Giriş Alanları:**
  - Ürün No (ürün kimliği)
  - Eklenecek Miktar (eklenecek miktar)
- **Güncelleme Mantığı:**
  - UPDATE ifadesi: `UPDATE Urunler SET Stok = Stok + ? WHERE UrunNo = ?`
  - Miktar > 0 olduğunu doğrular
  - Ürünün var olduğunu doğrular
  - Veritabanını günceller ve tabloyu yeniler
- **Doğrulama:** Negatif stok eklemelerini engeller, sayısal girişi doğrular
- **Gezinti:** Geri düğmesi giriş ekranına döner

### KullaniciEkle.java
**Amaç:** Sisteme yeni kullanıcı/kasiyer ekle
- **Giriş Alanları:**
  - Kullanici Adi (Kullanıcı adı)
  - Adi Soyadi (Tam ad)
  - Pin Kodu (PIN/Şifre)
  - Gorev (Rol açılır menüsü) - Seçenekler: "kasiyer", "admin"
- **Doğrulama:**
  - Tüm alanlar doldurulmalıdır
  - PIN sayısal olmalıdır
  - Uygun hata mesajlarını gösterir
- **Veritabanı İşlemi:**
  - INSERT ifadesi: `INSERT INTO Kullanici (KullaniciAdi, AdiSoyadi, PinKodu, Gorev) VALUES (?, ?, ?, ?)`
  - Otomatik artan HesapNo veritabanı tarafından atanır
  - Başarılı ekleme sonrası formu temizler
- **Erişim Kontrolü:** Yalnızca yönetici ekranından erişilebilir
- **Gezinti:** Geri düğmesi giriş ekranına döner

### KullaniciSil.java
**Amaç:** Sistemden kullanıcı/kasiyer silme güvenlik kontrolleriyle sil
- **Yapıcı:** Kendi kendini silmeyi önlemek için mevcut yönetici kullanıcı adını kabul eder
- **Görüntü:** Tüm kullanıcıları gösteren JTable (HesapNo, KullaniciAdi, AdiSoyadi, Gorev)
- **Silme Güvenlik Önlemleri:**
  - 'super-admin' rolündeki kullanıcıları silinmesini engeller
  - Yönetici kendi hesabını silemeyi engeller
  - HesapNo girdisi ve onay gerektirir
- **Silme Mantığı:**
  - Önce kullanıcı verilerini alır, varlığını ve rolünü doğrular
  - Silinmeden önce onay iletişim kutosunu gösterir
  - DELETE ifadesi: `DELETE FROM Kullanici WHERE HesapNo = ?`
  - Başarılı silme sonrası tabloyu yeniler
- **Hata İşleme:** Sayısal girişi doğrular, kullanıcı izinlerini kontrol eder
- **Gezinti:** Geri düğmesi giriş ekranına döner

### KasiyerEkran.java
**Amaç:** Alternatif kasiyer/satış arayüzü
- SatisEkran'a benzer işlevsellik
- Kasiyer işlemleri için alternatif ekran düzenini sağlar

### KasiyerEkle.java
**Amaç:** Yeni kasiyer kullanıcı ekle
- KullaniciEkle'ye benzer işlevsellik
- Kasiyer hesapları eklemek için alternatif arayüz sağlar

## Kullanıcı Rolleri ve Yetkileri (User Roles and Permissions)

### Super-Admin (Süper Yönetici)
**Yapabilecekleri:**
- Admin paneline erişim
- Yeni ürün ekleme
- Ürün silme
- Ürün bilgilerini düzenleme
- Ürün stok ekleme
- Yeni kullanıcı/kasiyer ekleme
- Admin ve kasiyer kullanıcılarını silme
- Satış geçmişini ve detaylarını görüntüleme

**Yapamayacakları:**
- Kendi hesabını silemez
- Başka super-admin kullanıcılarını silemez

**Varsayılan Giriş:** `admin` / `9999`

---

### Admin (Yönetici)
**Yapabilecekleri:**
- Admin paneline erişim
- Yeni ürün ekleme
- Ürün silme
- Ürün bilgilerini düzenleme
- Ürün stok ekleme
- Yeni kasiyer ekleme
- Kasiyer kullanıcılarını silme
- Satış geçmişini ve detaylarını görüntüleme

**Yapamayacakları:**
- Kendi hesabını silemez
- Super-admin kullanıcılarını silemez
- Diğer admin kullanıcılarını silemez
- Kasiyer arayüzüne erişemez

**Varsayılan Giriş:** `asli` / `8888`

---

### Kasiyer (Cashier)
**Yapabilecekleri:**
- Satış arayüzüne erişim
- Stokta bulunan ürünleri görüntüleme
- Satış işlemi yapma (ürün seçme ve miktar girme)
- Satış hesaplama
- Müşteri adı ekleme (isteğe bağlı)
- Satış onaylama ve gerçekleştirme
- Otomatik stok güncelleme

**Yapamayacakları:**
- Admin paneline erişemez
- Ürün ekleyemez, silemez veya düzenleyemez
- Stok ekleyemez
- Kullanıcı yönetimi yapamaz
- Satış geçmişini görüntüleyemez

**Varsayılan Girişler:**
- `ayse` / `1234`
- `mehmet` / `5678`

---

## Veritabanı Kurulumu

Temiz tablolar ve örnek veriler ayarlamak için aşağıdaki SQL komut dosyasını kullanın:

```SQL
-- Kullanici tablosunu silip yeniden oluştur

DROP TABLE Kullanici;

CREATE TABLE Kullanici (
HesapNo INTEGER PRIMARY KEY AUTOINCREMENT,
KullaniciAdi varchar(30),
AdiSoyadi varchar(30),
PinKodu Integer,
Gorev varchar(30)
);

INSERT INTO Kullanici (KullaniciAdi, AdiSoyadi, PinKodu, Gorev)
VALUES
	('admin', 'Admin', 9999, 'super-admin'),
	('asli', 'Asli Çelik', 8888, 'admin'),
	('ayse', 'Ayşe Kaya', 1234, 'kasiyer'),
	('mehmet', 'Mehmet Sönmez', 5678, 'kasiyer');

-- Urunler tablosunu silip yeniden oluştur

DROP TABLE Urunler;

CREATE TABLE Urunler (
UrunNo INTEGER PRIMARY KEY AUTOINCREMENT,
Ad varchar(30),
Fiyat DECIMAL(10, 2),
Stok INTEGER
);

INSERT INTO Urunler (Ad, Fiyat, Stok)
VALUES
	('Süt', 35.50, 150),
	('Ekmek', 15.00, 200),
	('Domates', 32.90, 75),
	('Beyaz Peynir', 150.75, 60),
	('Makarna', 18.25, 300),
	('Zeytinyağı', 260.00, 40),
	('Yumurta', 45.00, 120),
	('Elma', 19.95, 95),
	('Tavuk', 110.50, 35),
	('Soda', 22.75, 180),
	('Yoğurt', 48.50, 110),
	('Pirinç', 52.00, 250),
	('Kıyma', 380.00, 25),
	('Patates', 17.50, 130),
	('Soğan', 14.00, 140),
	('Deterjan', 215.90, 55),
	('Çay', 145.50, 220),
	('Şeker', 38.75, 400),
	('Un', 42.25, 350),
	('Salça', 55.00, 160),
	('Kahve', 49.90, 85),
	('Limon', 25.00, 90),
	('Salatalık', 28.50, 80),
	('Sucuk', 295.00, 30),
	('Kaşar Peyniri', 210.25, 50),
	('Su', 24.50, 500),
	('Çikolata', 27.80, 450),
	('Bisküvi', 19.00, 400),
	('Tereyağı', 95.75, 70),
	('Mercimek', 58.50, 280);
```

## Varsayılan Kimlik Bilgilerini Test Etme

- **Süper Yönetici Girişi:** Kullanıcı Adı: `admin` | PIN: `9999`
- **Kasiyer 1:** Kullanıcı Adı: `ayse` | PIN: `1234`
- **Kasiyer 2:** Kullanıcı Adı: `mehmet` | PIN: `5678`

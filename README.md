# BYS - Bilgi Yonetim Sistemi

Gaziantep Buyuksehir Belediyesi icin gelistirilen, vatandas sosyal hizmetlerini yonetmeye yonelik kurumsal web uygulamasi.

## Moduller

| Modul | Aciklama |
|-------|----------|
| **Sistem Yonetimi** | Kullanici, rol, yetki yonetimi (RBAC), duyurular, sistem ayarlari |
| **Genel** | Vatandas, personel, sehir/ilce/mahalle, egitim, hastalik, banka, kurs, anket kayitlari |
| **Engelsizler** | Engelli vatandas kayitlari, arac/tedavi gerec teslimi, gorusmeler, sosyal arastirma |
| **Engelsiz Kariyer Merkezi** | Kursiyer, kurs, is basvurulari, devam takibi |
| **Aktif Yasam** | Aktif yasam programlari, etkinlikler, saglik bilgileri, gunluk planlar |
| **Evim Dunyalara Bedel** | Yasli bakim basvurulari, hizmet planlama, memnuniyet anketleri |
| **Moral Evi** | Sosyal bakim merkezi - kisi kayitlari, etkinlikler, giris/cikis takibi |
| **Ileri Yas** | Yasli hizmetleri - kisi yonetimi, talep/konu takibi |
| **Ortez Protez** | Hasta kayitlari, olcum, malzeme talep, stok, fizik tedavi, randevu, proje yonetimi |
| **Saglik Hizmetleri** | Saglik randevulari, kisi saglik kayitlari, kan testi, obezite anketi |
| **Hafriyat** | Hafriyat isleri, firmalar, araclar, depo, fatura, kasa |

## Teknoloji

### Backend
- **Java 17** / **Jakarta EE 11**
- **Jakarta Faces (JSF) 4.0** + **PrimeFaces 15**
- **Hibernate ORM 6.6** (JPA) + **Hibernate Spatial**
- **Microsoft SQL Server** (veritabani)
- **JTA** (islem yonetimi)
- **Lombok**, **JJWT**, **jBCrypt**, **Jackson**, **Unirest**
- **JasperReports** / **Apache POI** / **Apache PDFBox** (raporlama)

### Frontend
- XHTML / Facelets + Bootstrap
- PrimeFaces zengin UI bilesenleri
- Swiper.js, Particles.js, Flatpickr, Choices.js

### Build & Deploy
- **Apache Maven** (cokuli proje yapisi)
- **GlassFish 7.x** veya **IBM WebSphere Liberty**
- WAR olarak dagitim

## Proje Yapisi

```
BYS/
??? pom.xml                          # Ana Maven POM (cokuli modul)
??? BilgiYonetimSistemi/             # Ana uygulama modulu (WAR)
?   ??? src/main/
?       ??? java/tr/bel/gaziantep/bysweb/
?       ?   ??? core/                # Cekirdek cerceve katmani
?       ?   ??? moduls/              # Is modulleri (11 modul)
?       ?   ??? webservice/          # Dis servis entegrasyonlari & REST API
?       ??? resources/
?       ??? webapp/                  # XHTML sayfalari, statik dosyalar
??? leafmap/                         # Ozel JSF bilesen kutuphanesi (JAR)
```

## On Gereksinimler

- Java 17 (JDK)
- Apache Maven
- Microsoft SQL Server
- Jakarta EE 11 uyumlu uygulama sunucusu (GlassFish 7.x veya WebSphere Liberty)
- JNDI DataSource: Uygulama sunucusunda `BYS` adinda SQL Server'a bagli kaynak

## Derleme

```bash
mvn clean package
```

Uretilen dosyalar:
- `leafmap/target/leafmap-1.0.0.jar` (ozel JSF bilesen kutuphanesi)
- `BilgiYonetimSistemi/target/BilgiYonetimSistemi-1.15.0.war` (ana uygulama)

## Kurulum & Calistirma

1. Uygulama sunucusunda `BYS` JNDI DataSource olusturun (SQL Server baglantisi).
2. WAR dosyasini sunucuya dagitin.
3. Dis konfigurasyon dosyasini olusturun:
    - Windows: `C:\BYS\config\application.properties`
    - Mac: `/Users/omerfarukkurt/DisBirimler/config/application.properties`
    - Ornek: `profile=test` (CAPTCHA atlama icin)
4. Uygulamaya erisim: `http://localhost:8080/BilgiYonetimSistemi/`
5. Varsayilan giris sayfasi: `giris.xhtml`

> **Not:** Hibernate `hbm2ddl.auto=validate` modundadir. Veritabani semasi onceden olusturulmali ve JPA varlik siniflariyla eslesmelidir.

## REST API

JWT tabanli kimlik dogrulama ile REST API sunulmaktadir.

- **Token:** `POST /api/auth/token`
- **Guvenlik:** IP beyaz listesi, oranti sinirlamasi (rate limiting), API loglama
- **Endpointler:** Engelsizler, Aktif Yasam, Evim Dunyalara Bedel, Kariyer Merkezi

## Dis Servis Entegrasyonlari

| Servis | Protokol | Aciklama |
|--------|----------|----------|
| **KPS** (Kimlik Paylasim Sistemi) | SOAP | T.C. Kimlik dogrulama, adres, mahalle, koordinat sorgulama |
| **Mezarlik** | REST | Vefat eden kisi sorgulama |
| **GaziKart** | - | Gaziantep toplulasim karti entegrasyonu |

## Harita Bileseni (leafmap)

[Leaflet.js](https://leafletjs.com/) uzerine insaa edilmis ozel JSF bileseni:
- Interaktif harita, isaretleyiciler, katmanlar
- Isi haritasi (heatmap) gorsellestirme
- Yol bulma (Leaflet Routing Machine)
- Geocoding, Tam ekran destegi

## Raporlama

JasperReports ile PDF/Excel rapor uretimi:
- Engelli hizmetleri: Talep formlari, bilgi formlari, dinamik raporlar, arac tamir formu
- Yasli bakim: Basvuru formlari, bilgi notlari
- Aktif yasam: ZiyaretcI kimlik kartlari (saglik, engellilik, acil durum iletisim, onay)

## Lisans

Bu proje Gaziantep Buyuksehir Belediyesi icin gelistirilmistir.
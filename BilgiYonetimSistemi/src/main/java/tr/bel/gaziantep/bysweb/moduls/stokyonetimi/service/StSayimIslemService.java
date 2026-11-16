package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStSayimDurum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StSayim;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StSayimKalem;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:51
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StSayimIslemService {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private StSayimService sayimService;
    @Inject
    private StSayimKalemService sayimKalemService;
    @Inject
    private StStokMiktarService miktarService;
    @Inject
    private StStokIslemService islemService;
    @Inject
    private StStokService stokService;

    public StSayim baslatSayim(StSayim sayim, List<StStok> stokKartlari) {
        if (sayim == null) throw new IllegalArgumentException("Sayým zorunludur");
        sayim.setDurum(EnumStSayimDurum.DEVAM);
        sayim.setBaslangicTarihi(LocalDateTime.now());
        sayimService.create(sayim);

        if (stokKartlari != null) {
            for (StStok sk : stokKartlari) {
                StSayimKalem kalem = new StSayimKalem();
                kalem.setStSayim(sayim);
                kalem.setStStok(stokService.find(sk.getId()));
                kalem.setStDepoLokasyon(sayim.getStDepoLokasyon());
                kalem.setStBirim(sk.getStBirim());
                BigDecimal sistem = miktarService.getOnHand(sk, sayim.getStDepo());
                kalem.setSistemMiktar(sistem);
                kalem.setSayilanMiktar(BigDecimal.ZERO);
                kalem.setFarkMiktar(BigDecimal.ZERO);
                sayimKalemService.create(kalem);
            }
        }
        return sayim;
    }

    public void sayimGir(StSayimKalem kalem, BigDecimal sayilanMiktar) {
        StSayimKalem k = em.find(StSayimKalem.class, kalem.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (k == null) throw new IllegalArgumentException("Sayým kalemi bulunamadý");
        k.setSayilanMiktar(sayilanMiktar == null ? BigDecimal.ZERO : sayilanMiktar);
        k.setFarkMiktar(k.getSayilanMiktar().subtract(k.getSistemMiktar()));
        sayimKalemService.edit(k);
    }

    public void farkHesapla(StSayim sayim) {
        StSayim s = em.find(StSayim.class, sayim.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (s == null) throw new IllegalArgumentException("Sayým bulunamadý");
        for (StSayimKalem k : s.getStSayimKalemList()) {
            k.setFarkMiktar(k.getSayilanMiktar().subtract(k.getSistemMiktar()));
            sayimKalemService.edit(k);
        }
    }

    public void onayla(StSayim sayim, SyKullanici onaylayan) {
        StSayim s = em.find(StSayim.class, sayim.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (s == null) throw new IllegalArgumentException("Sayým bulunamadý");

        for (StSayimKalem k : s.getStSayimKalemList()) {
            BigDecimal fark = k.getFarkMiktar() == null ? BigDecimal.ZERO : k.getFarkMiktar();
            if (fark.compareTo(BigDecimal.ZERO) == 0) {
                k.setOnaylandi(true);
                sayimKalemService.edit(k);
                continue;
            }
            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(k.getStStok());
            hareket.setStDepo(s.getStDepo());
            hareket.setStDepoLokasyon(k.getStDepoLokasyon());
            hareket.setHareketTuru(fark.compareTo(BigDecimal.ZERO) > 0 ? EnumStHareketTuru.SAYIM_FAZLASI : EnumStHareketTuru.SAYIM_EKSIGI);
            hareket.setDurum(fark.compareTo(BigDecimal.ZERO) > 0 ? EnumGirisCikis.GIRIS : EnumGirisCikis.CIKIS);
            hareket.setMiktar(fark.abs());
            hareket.setStBirim(k.getStBirim());
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setSyKullanici(onaylayan);
            hareket.setStSayim(s);
            hareket.setBelgeTuru("STSAYIM");
            hareket.setBelgeId(s.getId());
            hareket.setBelgeNo(s.getSayimNo());
            islemService.createMovement(hareket);

            k.setOnaylandi(true);
            sayimKalemService.edit(k);
        }

        s.setOnaylayanKullanici(onaylayan);
        s.setBitisTarihi(LocalDateTime.now());
        s.setDurum(EnumStSayimDurum.ONAYLANDI);
        sayimService.edit(s);
    }

    public void iptal(StSayim sayim) {
        StSayim s = em.find(StSayim.class, sayim.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (s == null) throw new IllegalArgumentException("Sayým bulunamadý");
        s.setDurum(EnumStSayimDurum.IPTAL);
        sayimService.edit(s);
    }
}

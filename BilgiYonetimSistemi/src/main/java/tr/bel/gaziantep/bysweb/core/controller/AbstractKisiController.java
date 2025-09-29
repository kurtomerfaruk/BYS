package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 10:02
 */
@Slf4j
public abstract class AbstractKisiController<T> extends AbstractController<T> {

    @Serial
    private static final long serialVersionUID = 4162011782357504807L;

    @Inject
    protected GnlKisiService kisiService;

    @Inject
    protected KpsController kps;

    @Getter
    @Setter
    private Class<T> itemClass;

    public AbstractKisiController() {
        super();
    }

    public AbstractKisiController(Class<T> itemClass) {
      super(itemClass);
    }

    protected void fetchTcKimlik(Function<T, GnlKisi> kisiExtractor,
                                 BiConsumer<T, GnlKisi> kisiSetter,
                                 EnumModul modul) {
        if (this.getSelected() != null) {
            try {
                GnlKisi gnlKisi = kisiExtractor.apply(this.getSelected());

                String tcKimlikNo = gnlKisi.getTcKimlikNo();
                LocalDate dogumTarihi = gnlKisi.getDogumTarihi();

                GnlKisi kisi = kisiService.findByTckimlikNoByDogumTarihi(tcKimlikNo, dogumTarihi);
                if (kisi == null) kisi = gnlKisi;

                kisi.setAktif(Boolean.TRUE);
                kisi = kps.findByTcKimlikNo(kisi, modul);

                kisiSetter.accept(this.getSelected(), kisi);

            } catch (Exception ex) {
                log.error("TC Kimlik sorgulama hatası", ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }
}

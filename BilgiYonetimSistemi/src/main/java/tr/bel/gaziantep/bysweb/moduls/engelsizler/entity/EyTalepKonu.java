package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.EnumUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Getter
@Setter
@Entity
@Table(name = "EYTALEP_KONU")
@NamedQuery(name = "EyTalepKonu.findByModul", query = "SELECT e FROM EyTalepKonu e WHERE e.aktif=true AND e.modeller LIKE :modul ORDER BY e.tanim")
public class EyTalepKonu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5019310262924611824L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 150)
    @Column(name = "TANIM", length = 150)
    private String tanim;

    @Size(max = 250)
    @Column(name = "MODELLER", length = 250)
    private String modeller;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<EnumModul> modulList;

    public List<EnumModul> getModulList() {
        if(StringUtils.isBlank(modeller)) return Collections.emptyList();
        String temp = StringUtil.removeBracket(modeller);
        if (!StringUtil.isBlank(temp)) {
            modulList = new ArrayList<>();
            modulList = Arrays.stream(temp.split(","))
                    .map(EnumModul::valueOf)
                    .collect(Collectors.toList());
        }
        return modulList;
    }

    public void setModulList(List<EnumModul> modulList) {
        this.modulList = modulList;
        this.modeller = EnumUtil.enumListToString(modulList);
    }


    public String getModulStr() {
        if (StringUtil.isBlank(modeller)) {
            return "";
        }
        modeller = StringUtil.removeBracket(modeller);
        return Arrays.stream(modeller.split(","))
                .map(EnumModul::valueOf)
                .map(EnumModul::getDisplayValue)
                .collect(Collectors.joining(","));
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyTalepKonu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}
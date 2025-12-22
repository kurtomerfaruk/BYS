package tr.bel.gaziantep.bysweb.core.dtos;

import lombok.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.12.2025 09:54
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityInfo {

    private String entityName;
    private String className;
}

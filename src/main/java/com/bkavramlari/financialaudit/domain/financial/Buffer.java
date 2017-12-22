package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "BUFFER")
public class Buffer extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KITAP_ID")
    @SequenceGenerator(name = "SEQ_KITAP_ID", sequenceName = "SEQ_KITAP_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "YEVMIYENO", nullable = false)
    private Long yevmiyeno;
    @Column(name = "MUHASEBEHESAPKODU", nullable = false)
    private String muhasebehesapkodu;

    public Buffer(Long yevmiyeno, String muhasebehesapkodu, long uploadid) {
        this.yevmiyeno = yevmiyeno;
        this.muhasebehesapkodu = muhasebehesapkodu;
        this.uploadid = uploadid;
    }

}

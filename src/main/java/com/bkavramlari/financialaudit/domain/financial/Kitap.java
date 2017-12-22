package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "KITAP")
public class Kitap extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KITAP_ID")
    @SequenceGenerator(name = "SEQ_KITAP_ID", sequenceName = "SEQ_KITAP_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "MUHASEBEKODU", nullable = false)
    private String muhasebekodu;
    @Column(name = "CARIKODU")
    private String carikodu;
    @Column(name = "VERGINO", nullable = false)
    private String vergino;
    @Column(name = "UNVAN", nullable = false)
    private String unvan;
    @Column(name = "VERGIDAIRESI")
    private String vergidairesi;

}

package fru1t.fru1tboard.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "admin")
@Entity
@Getter
@ToString
@NoArgsConstructor
public class Admin {
    @Id
    private Long adminId;
}

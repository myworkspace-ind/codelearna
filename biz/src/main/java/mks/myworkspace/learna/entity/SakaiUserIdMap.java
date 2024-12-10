package mks.myworkspace.learna.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sakai_user_id_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SakaiUserIdMap {
    @Id
    @Column(name = "USER_ID", length = 99)
    private String userId;

    @Column(name = "EID", nullable = false)
    private String eid;

	
}


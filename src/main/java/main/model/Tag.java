package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tags")
public class Tag {

  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @NotNull
  private String name;

}

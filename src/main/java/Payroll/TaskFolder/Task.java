package Payroll.TaskFolder;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Task {

  private @Id @GeneratedValue Long id;
  private String name;
  private String status;

  Task() {}

  public Task(String name, String status) {

    this.name = name;
    this.status = status;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return this.status;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Task))
      return false;
    Task task = (Task) o;
    return Objects.equals(this.id, task.id) && Objects.equals(this.name, task.name) && Objects.equals(this.status, task.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.status);
  }

  @Override
  public String toString() {
    return "Task{" + "id=" + this.id + ", name='" + this.name + '\'' + ", status='" + this.status
        + '\'' + '}';
  }
}
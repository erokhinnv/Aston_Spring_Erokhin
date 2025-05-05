package universities.entities;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Department {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Collection<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(Collection<Professor> professors) {
        this.professors = professors;
    }

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private University university;
    @Column(nullable = false)
    private String name;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "department")
    private Collection<Professor> professors;
}

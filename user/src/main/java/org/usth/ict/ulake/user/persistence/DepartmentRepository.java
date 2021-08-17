package org.usth.ict.ulake.user.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.usth.ict.ulake.user.model.Department;
import org.usth.ict.ulake.user.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {
}
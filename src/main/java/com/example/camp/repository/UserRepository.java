package com.example.camp.repository;

import com.example.camp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    Users findByEmail(java.lang.String email);
}

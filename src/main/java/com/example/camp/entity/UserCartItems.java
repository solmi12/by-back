    package com.example.camp.entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.List;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserCartItems {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long userCartId;

        @OneToMany(mappedBy = "userCartItems", cascade = CascadeType.ALL)
        private List<CartItem> cartItems;

        private Double totalOfTotalPrice;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "user_id")
        private Users user;

        public UserCartItems(Users user) {
            this.user = user;
            // Initialize other fields as needed
        }
    }


    package com.example.camp.dto;


    import jakarta.persistence.Column;
    import jakarta.persistence.Lob;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserDTO {

        private Integer userId;

        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String birthday;
        private Integer phoneNumber;
        @Lob
        @Column(columnDefinition = "MEDIUMBLOB")
        private String imageData;
        private String role;
    }

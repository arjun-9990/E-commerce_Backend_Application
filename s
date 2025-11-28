[33mcommit ed1806dce6a48d6f1d50400df23a66bbda58534f[m
Author: Arjun Gaikwad <arjungaikwad9320@gmail.com>
Date:   Fri Nov 28 11:44:51 2025 +0530

    add Payment entity with required fields and relationships

[1mdiff --git a/src/main/java/com/ecommerce/Model/Payment.java b/src/main/java/com/ecommerce/Model/Payment.java[m
[1mnew file mode 100644[m
[1mindex 0000000..49eeaff[m
[1m--- /dev/null[m
[1m+++ b/src/main/java/com/ecommerce/Model/Payment.java[m
[36m@@ -0,0 +1,42 @@[m
[32m+[m[32mpackage com.ecommerce.Model;[m
[32m+[m
[32m+[m[32mimport jakarta.persistence.*;[m
[32m+[m[32mimport jakarta.validation.constraints.NotBlank;[m
[32m+[m[32mimport jakarta.validation.constraints.Size;[m
[32m+[m[32mimport lombok.AllArgsConstructor;[m
[32m+[m[32mimport lombok.Data;[m
[32m+[m[32mimport lombok.NoArgsConstructor;[m
[32m+[m
[32m+[m[32m@Entity[m
[32m+[m[32m@Table(name = "payments")[m
[32m+[m[32m@Data[m
[32m+[m[32m@NoArgsConstructor[m
[32m+[m[32m@AllArgsConstructor[m
[32m+[m[32mpublic class Payment {[m
[32m+[m
[32m+[m[32m    @Id[m
[32m+[m[32m    @GeneratedValue(strategy = GenerationType.IDENTITY)[m
[32m+[m[32m    private Long paymentId;[m
[32m+[m
[32m+[m[32m    @OneToOne(mappedBy = "payment", cascade = { CascadeType.PERSIST, CascadeType.MERGE })[m
[32m+[m[32m    private Order order;[m
[32m+[m
[32m+[m[32m    @NotBlank[m
[32m+[m[32m    @Size(min = 4, message = "Payment method must contain at least 4 characters")[m
[32m+[m[32m    private String paymentMethod;[m
[32m+[m[41m    [m
[32m+[m[32m    // paymentGatway[m
[32m+[m[32m    private String pgPaymentId;[m
[32m+[m[32m    private String pgStatus;[m
[32m+[m[32m    private String pgResponseMessage;[m
[32m+[m
[32m+[m[32m    private String pgName;[m
[32m+[m
[32m+[m[32m    public Payment(Long paymentId, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {[m
[32m+[m[32m        this.paymentId = paymentId;[m
[32m+[m[32m        this.pgPaymentId = pgPaymentId;[m
[32m+[m[32m        this.pgStatus = pgStatus;[m
[32m+[m[32m        this.pgResponseMessage = pgResponseMessage;[m
[32m+[m[32m        this.pgName = pgName;[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
\ No newline at end of file[m

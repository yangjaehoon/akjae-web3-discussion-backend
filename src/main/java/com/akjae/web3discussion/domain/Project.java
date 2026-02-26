package com.akjae.web3discussion.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(unique = true, length = 200)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 블록체인 체인 (e.g. Ethereum, Solana, BSC)
    @Column(length = 100)
    private String chain;

    // 프로젝트 웹사이트
    @Column(length = 500)
    private String website;

    // 트위터/X 링크
    @Column(length = 500)
    private String twitterUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.ACTIVE;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    public enum ProjectStatus {
        ACTIVE,    // 아직 운영 중
        DEAD,      // 완전히 망함
        SUSPICIOUS // 의심스러움
    }
}

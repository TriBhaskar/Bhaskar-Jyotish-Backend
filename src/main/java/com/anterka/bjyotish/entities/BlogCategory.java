package com.anterka.bjyotish.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog_categories")
public class BlogCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_category_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "blog_category_sequence_generator", sequenceName = "seq_blog_category_id", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "slug", length = 100, nullable = false, unique = true)
    private String slug;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlogPost> blogPosts;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Utility methods
    public boolean isActive() {
        return isActive != null && isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public int getBlogPostsCount() {
        return blogPosts != null ? blogPosts.size() : 0;
    }

    public String getCategorySummary() {
        return String.format("%s (%d posts) - %s",
                name, getBlogPostsCount(), isActive() ? "Active" : "Inactive");
    }
}

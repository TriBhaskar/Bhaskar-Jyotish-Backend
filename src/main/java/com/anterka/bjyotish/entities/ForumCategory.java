package com.anterka.bjyotish.entities;

// ==============================================
// FORUM CATEGORY ENTITY
// ==============================================

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
@Table(name = "forum_categories")
public class ForumCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forum_category_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "forum_category_sequence_generator", sequenceName = "seq_forum_category_id", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ForumTopic> forumTopics;

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

    public int getTopicsCount() {
        return forumTopics != null ? forumTopics.size() : 0;
    }

    public int getActiveTopicsCount() {
        if (forumTopics == null) return 0;
        return (int) forumTopics.stream()
                .filter(topic -> !topic.isLocked())
                .count();
    }

    public int getTotalRepliesCount() {
        if (forumTopics == null) return 0;
        return forumTopics.stream()
                .mapToInt(ForumTopic::getRepliesCount)
                .sum();
    }

    public ForumTopic getLatestTopic() {
        if (forumTopics == null || forumTopics.isEmpty()) return null;
        return forumTopics.stream()
                .max((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                .orElse(null);
    }

    public String getCategorySummary() {
        return String.format("%s (%d topics, %d replies) - %s",
                name, getTopicsCount(), getTotalRepliesCount(),
                isActive() ? "Active" : "Inactive");
    }
}

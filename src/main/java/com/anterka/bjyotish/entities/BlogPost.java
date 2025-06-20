package com.anterka.bjyotish.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog_posts")
public class BlogPost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_post_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "blog_post_sequence_generator", sequenceName = "seq_blog_post_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private BjyotishUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private BlogCategory category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "excerpt", columnDefinition = "TEXT")
    private String excerpt;

    @Column(name = "featured_image_url")
    private String featuredImageUrl;

    @Column(name = "tags", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> tags;

    @Column(name = "is_published")
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "views_count")
    @Builder.Default
    private Integer viewsCount = 0;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public boolean isPublished() {
        return isPublished != null && isPublished;
    }

    public boolean isDraft() {
        return !isPublished();
    }

    public void publish() {
        this.isPublished = true;
        this.publishedAt = Instant.now();
    }

    public void unpublish() {
        this.isPublished = false;
        this.publishedAt = null;
    }

    public void incrementViews() {
        this.viewsCount = (this.viewsCount != null ? this.viewsCount : 0) + 1;
    }

    public boolean hasFeaturedImage() {
        return featuredImageUrl != null && !featuredImageUrl.trim().isEmpty();
    }

    public boolean hasExcerpt() {
        return excerpt != null && !excerpt.trim().isEmpty();
    }

    public boolean hasCategory() {
        return category != null;
    }

    public String getCategoryName() {
        return hasCategory() ? category.getName() : "Uncategorized";
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    public int getTagsCount() {
        return hasTags() ? tags.size() : 0;
    }

    public String getTagsString() {
        return hasTags() ? String.join(", ", tags) : "";
    }

    public boolean hasTag(String tag) {
        return hasTags() && tags.contains(tag);
    }

    public void addTag(String tag) {
        if (tags == null) {
            tags = new java.util.ArrayList<>();
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        if (hasTags()) {
            tags.remove(tag);
        }
    }

    public String getContentPreview(int maxLength) {
        if (hasExcerpt()) {
            return excerpt.length() > maxLength ? excerpt.substring(0, maxLength) + "..." : excerpt;
        }

        if (content == null || content.trim().isEmpty()) {
            return "No content available";
        }

        String cleanContent = content.replaceAll("<[^>]*>", "").trim(); // Remove HTML tags
        return cleanContent.length() > maxLength ? cleanContent.substring(0, maxLength) + "..." : cleanContent;
    }

    public int getContentWordCount() {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }

    public String getPostSummary() {
        return String.format("%s - %s (%d views)",
                title, isPublished() ? "Published" : "Draft", viewsCount);
    }

    public String getAuthorName() {
        return author != null ? author.getFirstName() : "Unknown Author";
    }
}

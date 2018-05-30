package com.emirovschi.sm.lab5.posts.models;

import com.emirovschi.sm.lab5.tags.models.TagModel;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "posts")
public class PostModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_id_seq")
    @GenericGenerator(name = "posts_id_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    private long id;

    private String title;

    @Column(columnDefinition = "BYTEA NOT NULL")
    private byte[] media;

    @Column(columnDefinition = "BYTEA NOT NULL")
    private byte[] preview;

    private String mediaType;

    @ManyToMany
    @JoinTable(name = "postTags", joinColumns = @JoinColumn(name = "post"), inverseJoinColumns = @JoinColumn(name = "tag"))
    private List<TagModel> tags;

    @ElementCollection
    @JoinTable(name = "votes", joinColumns = @JoinColumn(name = "post"))
    @MapKeyJoinColumn(name = "\"user\"")
    private Map<UserModel, Integer> votes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<CommentModel> comments;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "\"user\"")
    private UserModel user;

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public byte[] getMedia()
    {
        return media;
    }

    public void setMedia(final byte[] media)
    {
        this.media = media;
    }

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(final String mediaType)
    {
        this.mediaType = mediaType;
    }

    public List<TagModel> getTags()
    {
        return tags;
    }

    public void setTags(final List<TagModel> tags)
    {
        this.tags = tags;
    }

    public Map<UserModel, Integer> getVotes()
    {
        return votes;
    }

    public void setVotes(final Map<UserModel, Integer> votes)
    {
        this.votes = votes;
    }

    public List<CommentModel> getComments()
    {
        return comments;
    }

    public void setComments(final List<CommentModel> comments)
    {
        this.comments = comments;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(final UserModel user)
    {
        this.user = user;
    }

    public byte[] getPreview()
    {
        return preview;
    }

    public void setPreview(final byte[] preview)
    {
        this.preview = preview;
    }
}

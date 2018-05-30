package com.emirovschi.sm.lab5.posts.models;

import com.emirovschi.sm.lab5.users.models.UserModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "comments")
public class CommentModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_id_seq")
    @GenericGenerator(name = "comments_id_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post")
    private PostModel post;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "\"user\"")
    private UserModel user;

    private Date time;

    private String comment;

    public PostModel getPost()
    {
        return post;
    }

    public void setPost(final PostModel post)
    {
        this.post = post;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(final UserModel user)
    {
        this.user = user;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(final Date time)
    {
        this.time = time;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(final String comment)
    {
        this.comment = comment;
    }
}

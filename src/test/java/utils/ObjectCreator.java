package utils;

import com.valeraci.kuzyasocialnetwork.models.Comment;
import com.valeraci.kuzyasocialnetwork.models.FamilyStatus;
import com.valeraci.kuzyasocialnetwork.models.FileType;
import com.valeraci.kuzyasocialnetwork.models.MediaFile;
import com.valeraci.kuzyasocialnetwork.models.Post;
import com.valeraci.kuzyasocialnetwork.models.Role;
import com.valeraci.kuzyasocialnetwork.models.User;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import com.valeraci.kuzyasocialnetwork.models.enums.FileTypeTitle;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;

import java.util.HashSet;
import java.util.Set;

public class ObjectCreator {

    public static User createUser() {
        User user = new User();

        FamilyStatus familyStatus = createFamilyStatus(FamilyStatusTitle.SINGLE);

        user.setFamilyStatus(familyStatus);
        user.setLastName("Tester");
        user.setFirstName("Test");

        return user;
    }

    public static User createUser(String lastName, String firstName,
                                  FamilyStatusTitle familyStatusTitle) {
        User user = new User();

        FamilyStatus familyStatus = createFamilyStatus(familyStatusTitle);

        user.setFamilyStatus(familyStatus);
        user.setLastName(lastName);
        user.setFirstName(firstName);

        return user;
    }

    public static UserCredential createUserCredential() {
        UserCredential userCredential = new UserCredential();
        userCredential.setEmail("TestEmail");
        userCredential.setPassword("TestPassword");

        Role role = createRole(RoleTitle.ROLE_USER);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        userCredential.setRoles(roleSet);

        User user = createUser();

        userCredential.setUser(user);

        return userCredential;
    }

    public static UserCredential createUserCredential(String email, String password,
                                                      String lastName, String firstName,
                                                      FamilyStatusTitle familyStatusTitle) {
        UserCredential userCredential = new UserCredential();
        userCredential.setEmail(email);
        userCredential.setPassword(password);

        Role role = createRole(RoleTitle.ROLE_USER);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        userCredential.setRoles(roleSet);

        User user = createUser(lastName, firstName, familyStatusTitle);

        userCredential.setUser(user);

        return userCredential;
    }

    public static FamilyStatus createFamilyStatus(FamilyStatusTitle familyStatusTitle) {
        FamilyStatus familyStatus = new FamilyStatus();

        switch (familyStatusTitle) {
            case SINGLE -> familyStatus.setId(1);
            case MARRIED -> familyStatus.setId(2);
            case IN_A_RELATIONSHIP -> familyStatus.setId(3);
            case COMPLICATED -> familyStatus.setId(4);
        }

        familyStatus.setTitle(familyStatusTitle);
        return familyStatus;
    }

    public static Role createRole(RoleTitle roleTitle) {
        Role role = new Role();

        switch (roleTitle) {
            case ROLE_OWNER -> role.setId(1);
            case ROLE_ADMINISTRATOR -> role.setId(2);
            case ROLE_USER -> role.setId(3);
        }

        role.setTitle(roleTitle);
        return role;
    }

    public static Post createPost(User user) {
        Post post = new Post();
        post.setUser(user);
        post.setTitle("TestTitle");
        post.setText("TestTest");

        return post;
    }

    public static Post createPost(User user, String title, String text) {
        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setText(text);

        return post;
    }

    public static Post createPostWithMediaFiles(User user) {
        Post post = createPost(user);

        Set<MediaFile> mediaFiles = new HashSet<>();
        mediaFiles.add(createMediaFile(post));
        post.setMediaFiles(mediaFiles);

        return post;
    }

    public static Post createPostWithMediaFiles(User user, String title, String text, String path) {
        Post post = createPost(user, title, text);

        Set<MediaFile> mediaFiles = new HashSet<>();
        mediaFiles.add(createMediaFile(post, path));
        post.setMediaFiles(mediaFiles);

        return post;
    }

    public static MediaFile createMediaFile(Post post) {
        MediaFile mediaFile = new MediaFile();

        mediaFile.setFileType(createFileType(FileTypeTitle.MP4));
        mediaFile.setPath("testPath");
        mediaFile.setPost(post);

        return mediaFile;
    }

    public static MediaFile createMediaFile(Post post, String path) {
        MediaFile mediaFile = new MediaFile();

        mediaFile.setFileType(createFileType(FileTypeTitle.MP4));
        mediaFile.setPath(path);
        mediaFile.setPost(post);

        return mediaFile;
    }

    public static FileType createFileType(FileTypeTitle fileTypeTitle) {
        FileType fileType = new FileType();

        switch (fileTypeTitle) {
            case JPEG -> fileType.setId(1);
            case PNG -> fileType.setId(2);
            case GIF -> fileType.setId(3);
            case MP4 -> fileType.setId(4);
        }

        fileType.setTitle(fileTypeTitle);
        return fileType;
    }

    public static Comment createComment(Post post, User commentator) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setText("TextText");
        comment.setCommentator(commentator);

        return comment;
    }

    public static Comment createComment(Post post, User commentator, String text) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setText(text);
        comment.setCommentator(commentator);

        return comment;
    }
}

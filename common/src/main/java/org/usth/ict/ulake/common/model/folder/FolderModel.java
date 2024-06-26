package org.usth.ict.ulake.common.model.folder;

import java.util.List;

public class FolderModel {
    public Long id;
    public String name;
    public Long ownerId;
    public Long creationTime;

    public List<FolderModel> subFolders;
    public List<FileModel> files;

    public FolderModel parent;

    public FolderModel() {}

    public FolderModel(
        Long id, String name, Long ownerId, Long creationTime,
        List<FolderModel> subFolders, List<FileModel> files,
        FolderModel parent) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.creationTime = creationTime;
        this.subFolders = subFolders;
        this.files = files;
        this.parent = parent;
    }
}

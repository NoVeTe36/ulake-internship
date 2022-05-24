/**
 * Data model for file representation model
 */


class FileSystem {
    constructor() {
        this.path = [];
    }

    /**
     * Support move of folder to 1 level deeper
     * If pathName is absolute path then re-assign it to model
     * @param {FolderModel} path
     */
    moveIn(path) {
        this.path = this.path.push(path);
    }

    /**
     * Support move out of folder
     * @param {String} level of folder to jump out
     */
    moveOut(level) {
        for (let i = 0; i < level; i++)
            this.path.pop();
    }

    toString() {
        let pathStr = "/";
        for (let p of this.path) {
            pathStr += p.name;
        }
        return pathStr;
    }

    /**
     * get folder model following index
     * @param {Integer} idx index of folder
     * @return {FolderModel}
     */
    get(idx) {
        if (this.path.length > 0)
            return this.path[idx];
        else
            return undefined;
    }

    size() {
        return this.path.length;
    }
}

Release Steps
=============
1. Increment version in develop.
2. Update CHANGELOG with changes.
3. Update README (features, latest dep version, etc...)
4. Clean project.
5. Publish to maven (using publishing -> bintrayUpload gradle task).
6. Tag release commit.
7. Create release branch.
8. Merge to master.
9. Create Git Release from the version tag.
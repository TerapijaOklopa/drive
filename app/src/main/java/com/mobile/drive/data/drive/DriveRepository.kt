package com.mobile.drive.data.drive

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.mobile.drive.mobile.utils.suspendRunCatching

class DriveRepositoryImpl(private val driveService: Drive) : DriveRepository {

    override suspend fun getFolderFiles(folderId: String): Result<List<File>> {
        suspendRunCatching {
            val files = mutableListOf<File>()
            var pageToken: String? = null
            do {
                val result = driveService.files().list()
                    .setQ("'$folderId' in parents")
                    .setFields("nextPageToken, files(id, name, mimeType, parents)")
                    .setPageToken(pageToken)
                    .execute()
                pageToken = result.nextPageToken
                files.addAll(result.files)
            } while (pageToken != null)
            return Result.success(files)
        }.getOrElse { t ->
            return Result.failure(t)
        }
    }

    override suspend fun searchFiles(query: String): Result<List<File>> {
        suspendRunCatching {
            val files = mutableListOf<File>()
            var pageToken: String? = null
            val searchQuery = if (query.isEmpty()) null else "name contains '$query'"
            do {
                val result = driveService.files().list()
                    .setQ(searchQuery)
                    .setFields("nextPageToken, files(id, name, mimeType, parents)")
                    .setPageToken(pageToken)
                    .execute()
                pageToken = result.nextPageToken
                files.addAll(result.files)
            } while (pageToken != null)
            return Result.success(files)
        }.getOrElse { t ->
            return Result.failure(t)
        }
    }
}

interface DriveRepository {
    suspend fun getFolderFiles(folderId: String): Result<List<File>>
    suspend fun searchFiles(query: String): Result<List<File>>
}

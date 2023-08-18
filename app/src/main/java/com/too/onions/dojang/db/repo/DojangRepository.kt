package com.too.onions.dojang.db.repo

import com.too.onions.dojang.db.DojangDB
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.data.User
import javax.inject.Inject

class DojangRepository @Inject constructor(private val db: DojangDB) {
    suspend fun insertPage(page: Page) {
        db.pageDao().insert(page)
    }
    suspend fun deletePage(page: Page) {
        db.pageDao().delete(page)
    }
    suspend fun getAllPage() : List<Page> {
        return db.pageDao().getAll()
    }


    // ==== Content ====

    suspend fun insertContent(content: Content) {
        db.contentDao().insert(content)
    }
    suspend fun deleteContent(content: Content) {
        db.contentDao().delete(content)
    }
    suspend fun deleteAllContent(pageId: Long) {
        db.contentDao().deleteAll(pageId)
    }
    suspend fun getAllContent(pageId: Long) : List<Content> {
        return db.contentDao().getAll(pageId)
    }


    // ==== User ====

    suspend fun getUser(email: String) : User {
        return db.userDao().get(email)
    }
    suspend fun insertUser(user: User) {
        db.userDao().insert(user)
    }
}
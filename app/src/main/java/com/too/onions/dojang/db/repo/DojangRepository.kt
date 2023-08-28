package com.too.onions.dojang.db.repo

import com.too.onions.dojang.db.DojangDB
import com.too.onions.dojang.db.data.Content
import com.too.onions.dojang.db.data.Page
import com.too.onions.dojang.db.data.Stamp
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.service.MainService
import javax.inject.Inject

class DojangRepository @Inject constructor(private val db: DojangDB) {
    suspend fun insertPage(page: Page) {
        db.pageDao().insert(page)
    }
    suspend fun updatePage(page: Page) {
        db.pageDao().update(page)
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
    suspend fun getContent(contentId: Long){
        db.contentDao().get(contentId)
    }
    suspend fun updateContent(content: Content) {
        db.contentDao().update(content)
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

    private var currentUser: User? = null

    fun getCurrentUser() : User? {
        return currentUser
    }
    suspend fun updateUserStamp(stamp: String) {
        if (currentUser != null) {
            currentUser = currentUser?.copy(stamp = stamp)
            db.userDao().updateStamp(stamp = stamp)
        }
    }
    suspend fun getUser(email: String) : User? {
        currentUser = db.userDao().get(email)
        return currentUser
    }
    suspend fun insertUser(user: User) {
        currentUser = user
        db.userDao().insert(user)
    }

}
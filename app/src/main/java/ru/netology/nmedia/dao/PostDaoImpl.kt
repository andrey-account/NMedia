package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post
import java.util.*

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LOOKS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} TEXT NOT NULL
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_LOOKS = "looks"
        const val COLUMN_SHARED = "shared"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_LOOKS,
            COLUMN_SHARED,
            COLUMN_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>() //Создаёт изменяемый список постов
        db.query(
            PostColumns.TABLE, //Имя таблицы
            PostColumns.ALL_COLUMNS, //Список столбцов
            null, //Фильтр
            null, //Аргументы фильтра
            null, //Группировка
            null,
            "${PostColumns.COLUMN_ID} DESC" //ID колонки, сортировка по убыванию
        ).use { //Закрывает курсор
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts //Возвращает список постов
    }


    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    private val author = "Андрей"
    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, author)  //"Me",
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, (getCurrentDateTime()).toString())  //"now"
            put(PostColumns.COLUMN_VIDEO, "https://www.youtube.com/watch?v=RbSRjkYERmg&list=PLkC6D3H7Qvssovds5jJWf41dW5IP9D8aN")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id) //Обрезает пустые строки и отступы, создаёт массив с id
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun look(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               looks = looks + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               shared = shared + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likes = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                shares = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_SHARED)),
                looks = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_LOOKS)),
                video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
            )
        }
    }
}
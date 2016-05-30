package com.xy.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.xie.ClientApplication;

import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

public class DBUtils {
  private static final String DEFAULT_DB_NAME = "xy_im_db";
  private static Book BOOK;

  static {
    Paper.init(ClientApplication.instance());
    create(DEFAULT_DB_NAME);
  }

  public static <T> Book write(@NonNull String key, @NonNull T value) {
    return BOOK.write(key, value);
  }

  public static <T> T read(@NonNull String key) {
    return read(key, null);
  }

  public static <T> T read(@NonNull String key, @Nullable T defaultValue) {
    return BOOK.read(key, defaultValue);
  }

  public static void delete(@NonNull String key) {
    BOOK.delete(key);
  }

  public static boolean exist(@NonNull String key) {
    return BOOK.exist(key);
  }

  public static List<String> getAllKeys() {
    return BOOK.getAllKeys();
  }

  public static void destroy() {
    BOOK.destroy();
  }

  public static void create(@NonNull String name) {
    BOOK = Paper.book(name);
  }
}

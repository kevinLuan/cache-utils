package com.extract.cache.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

public class SerializableUtils<T> {
  private static final Logger LOGGER = Logger.getLogger(SerializableUtils.class);

  public byte[] encode(T o) {
    byte[] result = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(new GZIPOutputStream(bos));
      oos.writeObject(o);
      oos.close();
      result = bos.toByteArray();
    } catch (IOException e) {
      LOGGER.error("catch io exception while change obj to byte !!!", e);
    } finally {
      try {
        if (null != oos) {
          oos.close();
        }
        bos.close();
      } catch (IOException e) {
        LOGGER.error("catch io exception while close resource for change obj to byte !!!");
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public T decode(byte[] bytes) {
    T obj = null;
    ObjectInputStream ois = null;
    if (null != bytes) {
      try {
        ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
        obj = (T) ois.readObject();
      } catch (Exception e) {
        LOGGER.error("catch io exception while change byte to obj !!!");
      } finally {
        try {
          if (null != ois) {
            ois.close();
          }
        } catch (IOException e) {
          LOGGER.error("catch io exception while close resource for change byte to obj !!!");
        }
      }
    }
    return obj;
  }
}

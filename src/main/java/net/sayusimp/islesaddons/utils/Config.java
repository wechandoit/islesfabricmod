package net.sayusimp.islesaddons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class Config extends AbstractMap<String, Object> {
    static final Gson GSON = (new GsonBuilder())
            .setPrettyPrinting()
            .create();

    private final Map<String, Object> internal;

    public Config() {
        this.internal = (Map<String, Object>)new LinkedTreeMap();
    }

    public Config(InputStream in) {
        try (Reader reader = reader(in)) {
            this.internal = (Map<String, Object>)GSON.fromJson(reader, Map.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Config(Map<String, Object> map) {
        this();
        this.internal.putAll(map);
    }

    public void load(File file) {
        load(file.toPath());
    }

    public void load(Path path) {
        try {
            load(new ByteArrayInputStream(Files.readAllBytes(path)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void load(InputStream in) {
        try (Reader reader = reader(in)) {
            this.internal.putAll((Map<? extends String, ?>)GSON.fromJson(reader, Map.class));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void load(Map<String, Object> map) {
        this.internal.putAll(map);
    }

    public void save(File file) {
        save(file.toPath());
    }

    public void save(Path path) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            save(out);
            Files.write(path, out.toByteArray(), new java.nio.file.OpenOption[0]);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void save(OutputStream out) {
        try (Writer writer = writer(out)) {
            GSON.toJson(this.internal, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void save(Map<String, Object> map) {
        map.putAll(this.internal);
    }

    public Object get(Object key) {
        return this.internal.get(key);
    }

    public <T> T get(String key, Class<T> type) throws IllegalStateException {
        Object value = this.internal.get(key);
        if (type == null || type.isInstance(value))
            return (T)value;
        throw new IllegalStateException("invalid value type " + value
                .getClass() + " for key " + key);
    }

    public Object put(String key, Object value) {
        return this.internal.put(key, value);
    }

    public void putAll(Map<? extends String, ?> m) {
        this.internal.putAll(m);
    }

    public Object remove(Object key) {
        return this.internal.remove(key);
    }

    @NotNull
    public Set<String> keySet() {
        return this.internal.keySet();
    }

    @NotNull
    public Collection<Object> values() {
        return this.internal.values();
    }

    @NotNull
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.internal.entrySet();
    }

    private static Reader reader(InputStream in) {
        Reader reader = new InputStreamReader(in);
        return (in instanceof ByteArrayInputStream) ? reader : new BufferedReader(reader);
    }

    private static Writer writer(OutputStream out) {
        Writer writer = new OutputStreamWriter(out);
        return (out instanceof ByteArrayOutputStream) ? writer : new BufferedWriter(writer);
    }
}


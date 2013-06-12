package org.powerbot.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.powerbot.util.StringUtil;

/**
 * @author Paris
 */
public class IOHelper {
	public static final int BUFFER_SIZE = 4096;

	public static byte[] read(final InputStream is) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			final byte[] temp = new byte[BUFFER_SIZE];
			int read;
			while ((read = is.read(temp)) != -1) {
				buffer.write(temp, 0, read);
			}
		} catch (final IOException ignored) {
			try {
				buffer.close();
			} catch (final IOException ignored2) {
			}
			buffer = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (final IOException ignored) {
			}
		}
		return buffer == null ? null : buffer.toByteArray();
	}

	public static byte[] read(final URL in) {
		try (final InputStream is = in.openStream()) {
			return read(is);
		} catch (final IOException ignored) {
			return null;
		}
	}

	public static byte[] read(final File in) {
		try (final InputStream is = new FileInputStream(in)) {
			return read(is);
		} catch (final IOException ignored) {
			return null;
		}
	}

	public static String readString(final InputStream is) {
		return StringUtil.newStringUtf8(read(is));
	}

	public static String readString(final URL in) {
		return StringUtil.newStringUtf8(read(in));
	}

	public static void write(final InputStream in, final OutputStream out) {
		try {
			final byte[] buf = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
		} catch (final IOException ignored) {
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException ignored) {
			}
		}
	}

	public static void write(final InputStream in, final File out) {
		try (final OutputStream os = new FileOutputStream(out)) {
			write(in, os);
		} catch (final IOException ignored) {
		}
	}

	public static void write(final String s, final File out) {
		final ByteArrayInputStream in = new ByteArrayInputStream(StringUtil.getBytesUtf8(s));
		write(in, out);
	}

	public static void write(final Map<String, byte[]> entries, final File out) {
		try (final ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(out))) {
			zip.setMethod(ZipOutputStream.STORED);
			zip.setLevel(0);
			for (final Map.Entry<String, byte[]> item : entries.entrySet()) {
				final ZipEntry entry = new ZipEntry(item.getKey() + ".class");
				entry.setMethod(ZipEntry.STORED);
				final byte[] data = item.getValue();
				entry.setSize(data.length);
				entry.setCompressedSize(data.length);
				entry.setCrc(IOHelper.crc32(data));
				zip.putNextEntry(entry);
				zip.write(item.getValue());
				zip.closeEntry();
			}
			zip.close();
		} catch (final IOException ignored) {
		}
	}

	public static long crc32(final InputStream in) {
		try (final CheckedInputStream cis = new CheckedInputStream(in, new CRC32())) {
			final byte[] buf = new byte[BUFFER_SIZE];
			while (cis.read(buf) != -1) {
				;
			}
			return cis.getChecksum().getValue();
		} catch (final IOException ignored) {
			return -1;
		}
	}

	public static long crc32(final byte[] data) throws IOException {
		try (final InputStream is = new ByteArrayInputStream(data)) {
			return crc32(is);
		} catch (final IOException ignored) {
			return -1;
		}
	}
}

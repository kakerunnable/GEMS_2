package file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SaveFile extends File {

	private byte[] saved = null;

	public String getSaved() {
		return new String(saved);
	}

	public SaveFile(File file) {
		this(file.toString());
	}

	public SaveFile(String pathname) {
		super(pathname);
		if (!super.isFile()) {
			throw new RuntimeException("ファイル以外は保存できません。");
		}
	}

	public void save() {
		try {
			saved = Files.readAllBytes(toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {

		if (saved == null) {
			throw new RuntimeException("保存されていません。");
		}

		try {
			Files.write(toPath(), saved);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

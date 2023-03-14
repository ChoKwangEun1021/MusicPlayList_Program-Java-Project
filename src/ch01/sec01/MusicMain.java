package ch01.sec01;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicMain {
	public static final int ADDSONG = 1, INFOSONG = 2, INFO_ALLSONG = 3, DELETESONG = 4, DELETE_ALLSONG = 5,
			UPDATESONG = 6, SORT = 7, PLAYLIST = 8, EXIT = 9;
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int no = 0;
		boolean run = true;

		ArrayList<Music> mlist = new ArrayList<Music>();
		DBConnection dbCon = new DBConnection();
		
		ArrayList<Music> playlist = new ArrayList<Music>();
		/* ��� �����ؼ� ���� ��� ��Ű��
		playlist.add(new Music("�뷡 ����", "����", 100,"���"));
		*/
		MusicPlayer mp = new MusicPlayer(playlist);

		while (run) {
			System.out.println("================================\t Music Playlist \t================================");
			System.out.println("[1]�뷡�߰� [2]�뷡 ���� ���� [3]��ü �뷡 ���� [4]�뷡 ���� [5]��ü �뷡 ���� [6]���� [7]���� [8]��� ��� [9]����");
			System.out.println("================================================================================================");
			System.out.print("���� > ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case ADDSONG:
				Music music = inputDataMusic(mlist);
				// �����ͺ��̽� �Է�
				int rValue = dbCon.insert(music);
				if (rValue == 1) {
					System.out.println("���Լ���");
				} else {
					System.out.println("���Խ���");
				}
				break;
			case INFOSONG:
				String dataTitle = searchSong();
				ArrayList<Music> list1 = dbCon.songSearchSelect(dataTitle);
				if (list1.size() >= 1) {
					printAllSong(list1);
				} else {
					System.out.println("�뷡���� �˻� ����");
				}
				break;
			case INFO_ALLSONG:
				ArrayList<Music> mlist2 = dbCon.select();
				if (mlist2 != null) {
					printAllSong(mlist2);
				} else {
					System.out.println("��� ����");
				}
				break;
			case DELETESONG:
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("���� ����");
				} else {
					System.out.println("���� ����");
				}
				break;
			case DELETE_ALLSONG:
				System.out.println("---------- ��ü �뷡 ���� ----------");
				System.out.println("�뷡�� ���� ���� �Ǿ����ϴ�.");
				dbCon.deleteAll();
				break;
			case UPDATESONG:
				int updateReturnValue = 0;
				int id = inputId();
				Music mlt = dbCon.selectId(id);
				if (mlt == null) {
					System.out.println("�������� �߻�");
				} else {
					Music updateSong = updateSong(mlt);
					updateReturnValue = dbCon.update(updateSong);
				}
				if (updateReturnValue == 1) {
					System.out.println("update ����");
				} else {
					System.out.println("update ����");
				}
				break;
			case SORT:
				ArrayList<Music> list3 = dbCon.selectSort();
				if (list3 == null) {
					System.out.println("���� ����");
				} else {
					printAllSong(list3);
					System.out.println("���� �Ϸ�");
				}
				break;
			case PLAYLIST:
				boolean onOff = true;
				while (onOff) {
					System.out.println("=========================\t Music Play \t=========================");
					System.out.println("[1]���  [2]����  [3]������  [4]������  [5]����");
					System.out.println("=================================================================================");
					switch (new Scanner(System.in).nextInt()) {
					case 1:
						Music currentMusic = mp.play();
						System.out.println(currentMusic.getTitle() + " ������Դϴ�.");
						break;
					case 2:
						mp.stop();
						System.out.println("�����մϴ�.");
						break;
					case 3:
						Music previouMusic = mp.previous();
						System.out.println(previouMusic.getTitle() + " ������Դϴ�.");
						break;
					case 4:
						Music nextMusic = mp.next();
						System.out.println(nextMusic.getTitle() + " ������Դϴ�.");
						break;
					case 5:
						System.out.println("ó�� �޴��� ���ư��ϴ�");
						onOff = false;
						break;
					}
				}
				break;
			case EXIT:
				run = false;
				break;
			}

		} // end of mains
		System.out.println("�����մϴ�.");
	}

	private static Music updateSong(Music mlist) {
		String title = inputSubject(mlist.getTitle() + ">>");
		mlist.setTitle(title);
		String singer = inputSubject(mlist.getSinger() + ">>");
		mlist.setSinger(singer);
		String genre = inputSubject(mlist.getGenre() + ">>");
		mlist.setGenre(genre);
		return mlist;
	}

	private static String inputSubject(String title) {
		boolean run = true;
		String data = null;
		while (run) {
			System.out.print(title + ">>");
			try {
				data = sc.nextLine();
				Pattern pattern = Pattern.compile("^[��-�R]{1,10}$");
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					run = false;
				} else {
					System.out.println("�߸��Է��Ͽ����ϴ�. ���Է¿�û");
				}
			} catch (NumberFormatException e) {
				System.out.println("�Է¿� ���� �߻�");
				data = null;
			}
		}
		return data;
	}

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID �Է�(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("id �Է� ����");
			}
		}
		return id;
	}

	private static Music inputDataMusic(ArrayList<Music> mlist) {
		String title = null;
		String singer = null;
		String genre = null;
		System.out.println("----- �뷡 �߰� -----");
		System.out.print("���� �Է� :");
		title = sc.nextLine();
		System.out.print("���� �Է� :");
		singer = sc.nextLine();
		System.out.print("�帣 �Է� :");
		genre = sc.nextLine();
		Music music = new Music(title, singer, genre);
		mlist.add(music);
		System.out.println("�뷡 �߰��� �Ϸ�Ǿ����ϴ�.");
		return music;
	}

	private static void printAllSong(ArrayList<Music> mlist) {
		System.out.println("---------- ��ü �뷡 ���� ----------");
		System.out.println("ID" + "\t" + "����" + "\t" + "����" + "\t" + "�帣");
		for (Music data : mlist) {
			System.out.print(data.getId() + "\t");
			System.out.print(data.getTitle() + "\t");
			System.out.print(data.getSinger() + "\t");
			System.out.print(data.getGenre() + "\t");
			System.out.println();
		}
	}

	private static String searchSong() {
		String title = null;
		title = matchingTitlePattern();
		return title;
	}

	private static String matchingTitlePattern() {
		String title = null;
		while (true) {
			try {
				System.out.print("�˻��� �뷡 ���� : ");
				title = sc.nextLine();
				Pattern pattern = Pattern.compile("^[��-�R]{1,10}$");
				Matcher matcher = pattern.matcher(title);
				if (!matcher.find()) {
					System.out.println("�����Է¿����߻� �ٽ����Է¿�û�մϴ�.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("�Է¿��� ������ �߻��߽��ϴ�.");
				break;
			}

		}
		return title;
	}

}

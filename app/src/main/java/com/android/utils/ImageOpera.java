package com.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.channels.FileChannel;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;


public class ImageOpera {
	public static String getCachePath() {
		File file = new File(getSDPath() + "Crane");
		if (!file.exists()) {
			file.mkdirs();
		}
		return getSDPath() + "Crane";
	}

	/**
	 * 获取路径
	 *
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存�?
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目�?
		}
		if (sdDir != null
				&& sdDir.toString().lastIndexOf("/") + 1 == sdDir.toString()
				.length()) {
			return sdDir.toString();
		} else {
			return sdDir.toString() + "/";
		}
	}







	/**
	 * 根据不同的加载图片显示不同�?�构造不同的选项，主要配置图片加载过程中显示配置，缓存，显示动画
	 */
	private DisplayImageOptions options;
	private static ImageOpera util;
	private ImageLoader loader;

	File cacheDir = new File(getCachePath() + "/imageloader");

	private ImageOpera(Context ctx) {

		ImageLoaderConfiguration cfg = new ImageLoaderConfiguration.Builder(ctx)
				.memoryCacheExtraOptions(800, 800) // default = device screen
						// dimensions
						// .diskCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,
						// null)
						// .taskExecutor(...)
						// .taskExecutorForCachedImages(...)
						// .threadPoolSize(3) // default
						// .threadPriority(Thread.NORM_PRIORITY - 1) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(getCacheSize()))//
						// .memoryCacheSize(2 * 1024 * 1024)
						// .memoryCacheSizePercentage(13) // default
				.diskCache(new UnlimitedDiskCache(cacheDir)) // default 自定义缓存路�?
						// .diskCacheSize(500 * 1024 * 1024)
						// .diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(ctx)) // default
						// .imageDecoder(new BaseImageDecoder()) // default
						// .writeDebugLogs()
				.defaultDisplayImageOptions(getDefaultOption()).build();
		loader = ImageLoader.getInstance();
		loader.init(cfg);
	}

	public static ImageOpera getInstance(Context ctx) {
		if (util == null) {
			util = new ImageOpera(ctx);
		}
		return util;
	}

	/**
	 * 内存缓存大小计算
	 *
	 * @return
	 */
	public static int getCacheSize() {
		// 获取到可用内存的�?大�?�，使用内存超出这个值会引起OutOfMemory异常�?
		// LruCache通过构�?�函数传入缓存�?�，以KB为单位�??
		int maxMemory = (int) (Runtime.getRuntime().maxMemory());
		// 使用�?大可用内存�?�的1/8作为缓存的大小�??
		int cacheSize = maxMemory / 4;
		Log.d("LruCache", cacheSize + "");
		return cacheSize;
	}

	/**
	 * 得到默认的option
	 *
	 * @return
	 */
	public DisplayImageOptions getDefaultOption() {
		options = new DisplayImageOptions.Builder().cacheInMemory(true)// 设置下载的图片是否缓存在内存�?
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
						// .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转�?
				.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类�?
				.build();
		return options;
	}

	/**
	 * 显示图片的所有配�?
	 * @return
	 */
	/*
	 * private DisplayImageOptions getWholeOptions() { DisplayImageOptions
	 * options = new DisplayImageOptions.Builder()
	 * .showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片
	 * .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
	 * .showImageOnFail(R.drawable.error) //设置图片加载/解码过程中错误时候显示的图片
	 * .cacheInMemory(true)//设置下载的图片是否缓存在内存�?
	 * .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中 .considerExifParams(true)
	 * //是否考虑JPEG图像EXIF参数（旋转，翻转�?
	 * .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
	 * .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类�?
	 * //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配�?
	 * .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时�?
	 * //设置图片加入缓存前，对bitmap进行设置 //.preProcessor(BitmapProcessor preProcessor)
	 * .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复�? .displayer(new
	 * RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多�? .displayer(new
	 * FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪�? .build();//构建完成
	 * 
	 * return options; }
	 */
	/**
	 * 重新设置option
	 *
	 * @param options
	 */
	public void setOption(DisplayImageOptions options, Context ctx) {
		this.options = options;
		ImageLoaderConfiguration cfg = new ImageLoaderConfiguration.Builder(ctx)
				.defaultDisplayImageOptions(options).build();
		loader.init(cfg);
	}

	public Bitmap loadImage(String uri, Context context) {
		if (isNetworkConnected(context)) {
			loader.denyNetworkDownloads(true);
		} else {
			loader.denyNetworkDownloads(false);
		}
		return loader.loadImageSync(uri);
	}

	/**
	 * 取消正在加载的任�?
	 *
	 * @param imageView
	 */
	public void cancelImageTask(ImageView imageView) {
		loader.cancelDisplayTask(imageView);
	}
	/**
	 * 获取加载�?
	 * @return
	 */
	public ImageLoader getLoader() {
		return loader;
	}

	public void loadImage(String uri, ImageView imageView) {
		loader.displayImage(uri, imageView);
	}

	public void loadImage(String uri, ImageView imageView,
						  DisplayImageOptions option) {
		if (options == null) {
			loader.displayImage(uri, imageView, this.options);
		} else {
			loader.displayImage(uri, imageView, option);
		}
	}


	public void loadImage(String uri, ImageView imageView,
						  ImageLoadingListener listener) {
		if (listener == null) {
			loader.displayImage(uri, imageView);
		} else {
			loader.displayImage(uri, imageView, getDefaultOption(), listener);
		}
	}

	public void loadImage(String uri, ImageView imageView,
						  DisplayImageOptions options, ImageLoadingListener listener) {
		if (listener == null) {
			loader.displayImage(uri, imageView, options);
		} else {
			loader.displayImage(uri, imageView, options, listener);
		}
	}

	public void loadImage(String uri, ImageView imageView,
						  DisplayImageOptions options, ImageLoadingListener listener,
						  ImageLoadingProgressListener progressListener) {
		if (progressListener == null) {
			loader.displayImage(uri, imageView, options, listener);
		} else {
			loader.displayImage(uri, imageView, options, listener,
					progressListener);
		}
	}

	public void clearImageCache() {
		clearImageCache(loader);
	}

	public void clearMemoryCache() {
		loader.clearMemoryCache();
	}

	@SuppressWarnings("deprecation")
	private void clearImageCache(ImageLoader loader) {
		loader.clearDiscCache();
		loader.clearMemoryCache();
	}

	/**
	 * bitmap保存到本地不压缩
	 *
	 * @param toPath
	 *            保存到的文件路径
	 * @param bitmap
	 *            png格式
	 */
	public static void compressBitmapToFile(String toPath, Bitmap bitmap) {
		File f = new File(toPath);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			bitmap.compress(CompressFormat.PNG, 100, fOut);// 将图像读取到fOut�?
		} catch (IOException e) {
			print("IO流异常");
			e.printStackTrace();
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				print("流关闭错误，IO流异常");
				e.printStackTrace();
			}
		}
	}

	/**
	 * bitmap保存到本地并压缩
	 *
	 * @param toPath
	 *            保存到的文件路径
	 * @param bitmap
	 *            png格式�?
	 * @param offset
	 *            压缩�? 1-100 例如 30 就是压缩70%
	 */
	public static void compressBitmapToFile(String toPath, Bitmap bitmap,
											int offset) {
		File f = new File(toPath);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			bitmap.compress(CompressFormat.PNG, offset, fOut);// 将图像读取到fOut�?
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 进行图片缩放和质量压�? 并写入文�?
	 *
	 * @param fromPath
	 *            源文�?
	 * @param toPath
	 *            目标文件
	 * @return 缩放过后的bitmap
	 * @throws Exception
	 */
	public static Bitmap compImageScale(String fromPath, String toPath)
			throws Exception {
		Bitmap bitmap = BitmapFactory.decodeFile(fromPath, new Options());
		FileOutputStream fops = new FileOutputStream(toPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		Log.e("LogUtil", "baos当前图片压缩为：" + baos.toByteArray().length / 1024
				+ "kb");
		Options newOpts = new Options();
		// �?始读入图片，此时把options.inJustDecodeBounds 设回true�?
		newOpts.inJustDecodeBounds = true;
		Bitmap map = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length, newOpts);
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		Log.e("LogUtil", "outWidth:" + w + "----------" + "outHeight:" + h);
		// 现在主流手机比较多是800*480分辨率，�?以高和宽我们设置�?
		float ww = 1280f;// 这里设置宽度�?600f
		float hh = 720f;// 这里设置高度�?800f
		// 缩放比�?�由于是固定比例缩放，只用高或�?�宽其中�?个数据进行计算即�?
		int be = 1;// be=1表示不缩�?
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		Log.e("LogUtil", "be:" + be);
		newOpts.inSampleSize = be;// 设置缩放比例
		map = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length, newOpts);
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		map.compress(CompressFormat.JPEG, 100, baos1);
		Log.e("LogUtil", "baos1当前图片压缩为：" + baos1.toByteArray().length / 1024
				+ "kb");
		fops.write(baos1.toByteArray());
		fops.flush();
		fops.close();
		return map;
	}

	/**
	 * @param
	 *
	 * @param
	 *
	 * @return 缩放过后的bitmap
	 * @throws Exception
	 */
	public static Bitmap compImageScale(Bitmap bitmap, String filePath)
			throws Exception {
		FileOutputStream fops = new FileOutputStream(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		Log.e("LogUtil", "baos当前图片压缩为：" + baos.toByteArray().length / 1024
				+ "kb");
		Options newOpts = new Options();
		// �?始读入图片，此时把options.inJustDecodeBounds 设回true�?
		newOpts.inJustDecodeBounds = true;
		Bitmap map = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length, newOpts);
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		Log.e("LogUtil", "outWidth:" + w + "----------" + "outHeight:" + h);
		// 现在主流手机比较多是800*480分辨率，�?以高和宽我们设置�?
		float ww = 1200f;// 这里设置宽度�?800f
		float hh = 720f;// 这里设置高度�?600f
		// 缩放比�?�由于是固定比例缩放，只用高或�?�宽其中�?个数据进行计算即�?
		int be = 1;// be=1表示不缩�?
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		Log.e("LogUtil", "be:" + be);
		newOpts.inSampleSize = be;// 设置缩放比例
		map = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length, newOpts);
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		map.compress(CompressFormat.JPEG, 100, baos1);
		Log.e("LogUtil", "baos1当前图片压缩为：" + baos1.toByteArray().length / 1024
				+ "kb");
		fops.write(baos1.toByteArray());
		fops.flush();
		fops.close();
		return map;
	}

	/**
	 * 质量压缩Bitmap质量并写入文�?
	 *
	 * @param image
	 *            bitmap对象
	 * @param filePath
	 *            文件路径
	 * @param size
	 *            文件大小（单位：kb�?
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static Bitmap compressToFile(Bitmap image, String filePath, int size)
			throws Exception {
		FileOutputStream fops = new FileOutputStream(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这�?100表示不压缩，把压缩后的数据存放到baos�?
		int options = 100;
		Log.e("LogUtil", "当前图片原大小为�?" + baos.toByteArray().length / 1024 + "kb");
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大�?(size)kb,大于继续压缩
			if (options < 0)
				return null;
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
			options -= 5;// 每次都减�?5
		}
		Log.e("LogUtil", "当前图片压缩为：" + baos.toByteArray().length / 1024 + "kb");
		fops.write(baos.toByteArray());
		fops.flush();
		fops.close();
		return BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length);
	}

	/**
	 * 压缩bitmap
	 *
	 * @param image
	 * @param size
	 * @return bitmap
	 * @throws Exception
	 */
	public static Bitmap compressBitmap(Bitmap image, int size)
			throws Exception {

		Options opts = new Options();
		opts.inSampleSize = 2;//图片宽高都为原来的3分之一，即图片为原来的9分之一
		opts.inJustDecodeBounds = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 70;
		Log.e("LogUtil", "当前图片原大小为：" + baos.toByteArray().length / 1024 + "kb");
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于(size)kb,大于继续压缩
			if (options < 0)
				return null;
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 5;// 每次都减少5
		}
		Log.e("LogUtil", "当前图片压缩为：" + baos.toByteArray().length / 1024 + "kb");
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
		/*return BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length);*/
	}

	/**
	 * 质量压缩Bitmap并写入文�?
	 *

	 * @param size
	 *            文件大小（单位：kb�?
	 * @return
	 * @throws Exception
	 */
	public static Bitmap compressToFile(String fromPath, String toPath, int size)
			throws Exception {
		Bitmap bitmap = BitmapFactory.decodeFile(fromPath, new Options());
		if (bitmap != null) {
			File file = new File(toPath);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fops = new FileOutputStream(toPath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这�?100表示不压缩，把压缩后的数据存放到baos�?
			int options = 50;
			while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大�?600kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				if (options < 5) {
					bitmap.compress(CompressFormat.JPEG, 5, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
					break;
				}
				if (options < 0) {
					bitmap.compress(CompressFormat.JPEG, 5, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
					break;
				}
				bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
				options = options / 2;
			}
			System.out.println("options的�??" + options);
			Log.e("LogUtil", "压缩后的图片大小为：" + baos.toByteArray().length / 1024
					+ "kb");
			fops.write(baos.toByteArray());
			fops.flush();
			fops.close();
			return BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
					baos.toByteArray().length);
		} else {
			System.out.println("获得bitmap的对象是�?");
			return null;
		}
	}



	public static String savePicToSdcard(Bitmap bitmap, String path,String fileName) {

		String filePath = "";
		if (bitmap == null) {

			return filePath;

		} else {

			filePath=path+ fileName;
			File destFile = new File(filePath);
			OutputStream os = null;
			try {

				os = new FileOutputStream(destFile);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.flush();
				os.close();

			} catch (IOException e) {

				filePath = "";
			}
		}
		return filePath;
	}



	/**
	 * 获取网络图片
	 *

	 * @param uri
	 * @return
	 */
	public Bitmap downloadImageFile(String uri) {
		URL newUrl;
		InputStream is = null;
		Bitmap download_bitmap = null;
		try {
			newUrl = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) newUrl
					.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(10000);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				download_bitmap = BitmapFactory.decodeStream(is);
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Log.i("123", "超时");
			return download_bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i("123", "url错误");
			return download_bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("123", "网络错误");
			return download_bitmap;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return download_bitmap;
	}


	/**
	 * Resize the bitmap
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}







	/**
	 * 获取网络图片
	 *

	 * @param uri
	 * @return
	 */
	public InputStream downloadStream(String uri) {
		URL newUrl;
		InputStream is = null;
		try {
			newUrl = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) newUrl
					.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(10000);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Log.i("123", "超时");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i("123", "url错误");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("123", "网络错误");
		}
		return is;
	}

	/**
	 * 加载本地图片 Bitmap http://bbs.3gstdy.com
	 *
	 * @param url
	 * @return
	 */
	public Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 文件拷贝
	 *
	 * @param s
	 *            源文�?
	 * @param t
	 *            新文�?
	 */
	public static void copyFile(File s, File t) {
		if (s==null || !s.exists()) {
			return;
		}
		if (t==null) {
			return;
		}
		if (!t.exists()) {
			try {
				t.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件�?�道
			out = fo.getChannel();// 得到对应的文件�?�道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断网络是否连接
	 *
	 * @param context
	 * @return true 连接�?
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static void print(String msg) {
		Log.e("ImageUtil", msg);
	}

	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 *
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
									   String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}



	/**
	 * 按钮按下动画
	 * @return
	 */
	public static Animation getScaleAnimation()
	{
		float end = 0.80f;
		float  start = 1.0f;
		final Animation scaleAnimation = new ScaleAnimation(start, end, start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		scaleAnimation.setDuration(100);
		scaleAnimation.setFillAfter(true);
		return scaleAnimation;
	}
	/**
	 * 按钮弹起动画
	 * @return
	 */
	public static Animation getEndAnimation()
	{
		float end = 0.80f;
		float  start = 1.0f;
		final Animation endAnimation = new ScaleAnimation(end, start, end, start,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		endAnimation.setDuration(100);
		endAnimation.setFillAfter(true);
		return endAnimation;
	}




	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
}

/***
  Copyright (c) 2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
 */

package com.commonsware.gapplayer;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.cordova.DroidGap;

public class gapPlayer extends DroidGap implements OnNavigationListener {
  private CheckAndExportTask task=null;
  private ArrayAdapter<String> nav=null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      setBooleanProperty("showTitle", true);
    }

    super.onCreate(savedInstanceState);

    task=(CheckAndExportTask)getLastNonConfigurationInstance();

    if (task == null) {
      initListNav();
      task=new CheckAndExportTask(this);
      task.execute();
    }
  }

  @Override
  public Object onRetainNonConfigurationInstance() {
    return(task);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.actions, menu);

    return(super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.refresh) {
      File index=
          new File(
                   getExternalFilesDir(null),
                   nav.getItem(getActionBar().getSelectedNavigationIndex())
                       + "/index.html");

      loadUrl(Uri.fromFile(index).toString());

      return(true);
    }

    return(super.onOptionsItemSelected(item));
  }

  @Override
  public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    loadUrl(Uri.fromFile(new File(getExternalFilesDir(null),
                                  nav.getItem(itemPosition)
                                      + "/index.html")).toString());

    return false;
  }

  void clearTask() {
    task=null;
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  private void initListNav() {
    ActionBar bar=getActionBar();

    ArrayList<String> projects=new ArrayList<String>();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      nav=
          new ArrayAdapter<String>(
                                   bar.getThemedContext(),
                                   android.R.layout.simple_spinner_item,
                                   projects);
    }
    else {
      nav=
          new ArrayAdapter<String>(
                                   this,
                                   android.R.layout.simple_spinner_item,
                                   projects);
    }

    nav.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    bar.setListNavigationCallbacks(nav, this);
  }

  private static class CheckAndExportTask extends
      AsyncTask<Void, Void, ArrayList<String>> {
    gapPlayer host=null;
    File ext=null;
    AssetManager mgr=null;

    CheckAndExportTask(gapPlayer host) {
      this.host=host;
      ext=host.getExternalFilesDir(null);
      mgr=host.getAssets();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
      ArrayList<String> projects=new ArrayList<String>();

      ext.mkdirs();

      if (ext.listFiles().length == 0) {
        publishProgress();

        try {
          copyAssetFileOrDir(mgr, "www", ext);
        }
        catch (IOException e) {
          Log.e(getClass().getSimpleName(), "Exception reading assets",
                e);
        }

        projects.add("www");
      }
      else {
        for (File f : ext.listFiles()) {
          projects.add(f.getName());
        }
      }

      return(projects);
    }

    @Override
    protected void onProgressUpdate(Void... params) {
      Toast.makeText(host, R.string.extracting_files,
                     Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(ArrayList<String> projects) {
      host.clearTask();

      for (String project : projects) {
        host.nav.add(project);
      }
    }
  }

  static private void copyAssetFileOrDir(AssetManager mgr, String path,
                                         File base) throws IOException {
    String[] assets=mgr.list(path);

    if (assets.length == 0) {
      copyAssetFile(mgr, path, base);
    }
    else {
      File out=new File(base, path);

      if (!out.exists()) {
        out.mkdir();
      }

      for (String asset : assets) {
        copyAssetFileOrDir(mgr, path + "/" + asset, base);
      }
    }
  }

  static private void copyAssetFile(AssetManager mgr, String path,
                                    File base) throws IOException {
    InputStream in=null;
    FileOutputStream out=null;

    in=mgr.open(path);
    out=new FileOutputStream(new File(base, path));

    byte[] buffer=new byte[1024];
    int read;

    while ((read=in.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }

    in.close();
    out.flush();
    out.getFD().sync();
    out.close();
  }
}

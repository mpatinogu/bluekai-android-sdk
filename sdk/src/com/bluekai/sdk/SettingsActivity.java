/*
 * Copyright 2013-present BlueKai, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluekai.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SettingsLayout settingsLayout = new SettingsLayout(this);
		setContentView(settingsLayout);

		Button cancel = settingsLayout.getCancel();
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		Button save = settingsLayout.getSave();
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean saved = settingsLayout.saveSettings();
				if (!saved) {
					Toast.makeText(getApplicationContext(), "Problem saving settings. Please try again later!!",
							Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
	}
}

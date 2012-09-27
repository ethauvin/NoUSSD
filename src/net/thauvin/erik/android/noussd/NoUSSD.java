/*
 * @(#)NoUSSD.java
 *
 * Copyright (c) 2012 Erik C. Thauvin (http://erik.thauvin.net/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the authors nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package net.thauvin.erik.android.noussd;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import net.thauvin.erik.android.noussd.R;

public class NoUSSD extends Activity
{
	private String appName;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		appName = getApplicationContext().getResources().getString(R.string.app_name);

		final Intent intent = getIntent();
		final Uri data = intent.getData();

		if (data != null)
		{
			final String uri = intent.getDataString();

			if ((uri.indexOf("%23") != -1) || (uri.indexOf('#') != -1) || (uri.indexOf('*') != -1))
			{
				final AlertDialog.Builder alert = new AlertDialog.Builder(this);
				
				alert.setTitle(R.string.dialog_title);
				alert.setIcon(R.drawable.ic_launcher);
				alert.setMessage(getString(R.string.dialog_msg, Uri.decode(data.getSchemeSpecificPart())));
				alert.setPositiveButton(R.string.dialog_ok, new OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dial(data);
						NoUSSD.this.finish();
					}
				});
				alert.setNegativeButton(R.string.dialog_cancel, new OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
						NoUSSD.this.finish();
					}
				});

				alert.show();
			}
			else
			{
				dial(data);
				finish();
			}
		}
		else
		{
			finish();
		}
	}

	private void dial(Uri callData)
	{
		try
		{
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(callData);
			startActivity(callIntent);
		}
		catch (ActivityNotFoundException e)
		{
			Log.e(this.appName, e.getLocalizedMessage());
		}
	}
}

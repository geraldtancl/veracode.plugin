# Veracode SAST IDE Plugin for Jetbrains IDEs (Community Plugin)

## Introduction
This is a very simple community plugin that I developed for comlimenting Veracode's official IDE integration with Jetbrains IDE products. It enables you to download the SAST result from Veracode Platform into your Jetbrains IDE and conveniently work within your day-to-day tool chain!

## Release Note
### Version [1.0](https://github.com/geraldtancl/veracode.plugin/blob/master/build/distributions/veracode.plugin-release-1.0.zip)
The very initial release which consists the core functionality of this plugin:
* Download policy / sandbox scan result
* Work with the flaw directly
* Automated scan upload and result download **(BETA Feature! More Info [here](https://github.com/geraldtancl/veracode.plugin/blob/master/README.md#automated-scan-upload-and-download-beta-feature))**


## "Supported" Jetbrains IDEs and Versions
Unlike official supported IDE plugins by Veracode, this is a self-initiated project - I am unable to test it against **all** Jetbrains IDE products. But, the information below is gathered from customers who are using it!

The plugin is known working with the following IDEs:
* IntelliJ
* Rider
* Pycharm
* PhpStorm
* AppCode
* Android Studio
* (maybe more... let me know if you discover any ;) )

The plugin is known working with the version up to #IC-211.*

## Installation
1. Download the latest version of the plugin from [distributions folder](https://github.com/geraldtancl/veracode.plugin/tree/master/build/distributions)
2. Follow this [instruction](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_disk) to get it installed.

## Configuration
1. [Generate your API credentials](https://help.veracode.com/r/t_create_api_creds)
2. Launch any action on the plugin. The plugin will request for your API credential automatically if it did not find any stored credential
3. All set.

## Usage
* Download scan result (policy scan / sandbox scan) manually
  ![Manual download scan result 1](https://github.com/geraldtancl/veracode.plugin/blob/master/docs/images/Manual_Download_Menu.png)
  ![Manual download scan result 2](https://github.com/geraldtancl/veracode.plugin/blob/master/docs/images/Result_Selector.png)
* Shows only flaws affecting compliance policy
  ![Flaws affecting compliance](https://github.com/geraldtancl/veracode.plugin/blob/master/docs/images/Flaw_Affecting_Policy.png)
* Act on the flaw directly via various option, such as commeting, mitigation
  ![Act on flaw](https://github.com/geraldtancl/veracode.plugin/blob/master/docs/images/Act_on_Finding.png)
  
## Known Issue
* You may very likely encounter this issue - when select a flaw from the tree, the flaw description is not shown. This is a known problem which occurred after a certain version of IDE base platform. I am still trying hard to get this resolved. **Workaround: Click on "Comments / Mitigations" tab then toggle back to "Description" again.**

## Roadmap
1. To add flaw actions "Reported to Library Maintainer" and "Accept the Risk" 
2. To change the authentication using credential file
3. Pipeline Scan (Potentially)

## Feedback / Request
File it to ["Issues"](https://github.com/geraldtancl/veracode.plugin/issues)!

## Automated Scan Upload and Download (BETA Feature)
This feature aims to enable developer for kicking off scanning and downloading result in a single click! 

Below is the steps to use this feature.
1. Copy the [veracode.config] file and place it at the root of your project (Please note that you will have to maintain the naming convention as such). You will see that the "Scan Application" and "Download Result" menu items are enabled.
2. Open the veracode.config, add / remove the config block (a config block looks like the following).
   ```
   {
    "config_name": "Policy Scan Config",
    "branch_pattern": "master",
    "static_config": {
      "scan_type": "policy",
      "sandbox_name": "",
      "scan_naming": "timestamp",
      "upload_include_patterns": [
        ".*.php",
        ".*.module",
        ".*.inc",
        ".*.html",
        ".*.htm",
        ".*.profile",
        ".*.install",
        ".*.engine",
        ".*.theme",
        ".*.php4",
        ".*.php5",
        ".*.php7",
        ".*.phtml"
      ],
      "upload_exclude_patterns": [
      ]
    },
    "portfolio": {
      "app_name": "Test PHP"
    }
   },
   ```
3. as
4. sd

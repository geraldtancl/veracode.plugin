# Veracode SAST IDE Plugin for Jetbrains IDEs (Community Plugin)

## Introduction
This is a very simple community plugin that I developed for comlimenting Veracode's official IDE integration with Jetbrains IDE products. It enables you to download the SAST result from Veracode Platform into your Jetbrains IDE and conveniently work within your day-to-day tool chain!

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

# GameNative Quick Setup and First Run Guide

Welcome to **GameNative**! This guide will help you get started with installing and running Windows games from your Steam library on Android devices using Wine and FEXCore emulation.

---

## 📋 Table of Contents

1. [Requirements](#-requirements)
2. [Installation](#-installation)
3. [First Launch](#-first-launch)
4. [Installing Your First Game](#-installing-your-first-game)
5. [Playing Games](#-playing-games)
6. [Configuration & Settings](#-configuration--settings)
7. [Troubleshooting](#-troubleshooting)
8. [Getting Help](#-getting-help)

---

## 📱 Requirements

### Device Requirements
- **Android Version:** Android 11 or higher recommended
- **RAM:** 6GB+ recommended (4GB minimum)
- **Storage:** At least 10GB free space for installation and games
- **CPU Architecture:** ARM64 (most modern Android devices)

### Account Requirements
- **Steam Account:** You'll need a valid Steam account to access your game library
- **Internet Connection:** Required for initial setup, login, and downloading games
  - Wi-Fi recommended for large game downloads
  - Can play installed games offline after initial setup

### Permissions Required
GameNative will request the following permissions:
- **Storage Access:** To install and manage game files
- **Notifications:** To show download progress and updates (Android 13+)

---

## 📥 Installation

### Step 1: Download the APK
1. Visit the [GameNative Releases page](https://github.com/gabrielnovais/GameNative/releases)
2. Download the latest APK file (e.g., `GameNative-v1.x.x.apk`)
3. If you see a security warning, this is normal for apps installed outside the Play Store

<img src="Pictures%20for%20guide/Screenshot_20251102_114001_Samsung%20Internet.jpg" alt="GitHub Releases Page" width="400">

*Scrolling through the GitHub releases page*

<img src="Pictures%20for%20guide/Screenshot_20251102_114013_Samsung%20Internet.jpg" alt="Release Assets" width="400">

*Viewing the release page with available downloads*

<img src="Pictures%20for%20guide/Screenshot_20251102_114025_Samsung%20Internet.jpg" alt="Release Assets List" width="400">

*List of APK files available for download*

<img src="Pictures%20for%20guide/Screenshot_20251102_114047_Samsung%20Internet.jpg" alt="Download Security Dialog" width="400">

*Browser security warning when downloading the APK file*

<img src="Pictures%20for%20guide/Screenshot_20251102_114146_Samsung%20Internet.jpg" alt="Download Notification" width="400">

*APK download complete notification*

### Step 2: Enable Installation from Unknown Sources
If this is your first time installing an APK manually:

1. Go to **Settings** → **Security** (or **Privacy**)
2. Enable **Install from Unknown Sources** or **Allow from this source** for your file manager/browser
3. Alternatively, modern Android will prompt you when you try to install

### Step 3: Install the APK
1. Open your **Downloads** folder or notification
2. Tap the downloaded APK file
3. Tap **Install**
4. Wait for installation to complete
5. Tap **Open** or find GameNative in your app drawer

<img src="Pictures%20for%20guide/Screenshot_20251102_114158_Package%20installer.jpg" alt="Install Confirmation" width="400">

*Confirming installation of GameNative*

<img src="Pictures%20for%20guide/Screenshot_20251102_114207_Google%20Play%20Store.jpg" alt="Google Play Protect Warning 1" width="400">

*Google Play Protect warning (tap "Install anyway")*

<img src="Pictures%20for%20guide/Screenshot_20251102_114213_Google%20Play%20Store.jpg" alt="Google Play Protect Warning 2" width="400">

*Additional Play Protect warning with "Install anyway" option*

<img src="Pictures%20for%20guide/Screenshot_20251102_114226_Package%20installer.jpg" alt="Installation Complete" width="400">

*Installation complete - tap "Open" to launch*

---

## 🚀 First Launch

### Initial Setup (First Time Only)

When you first open GameNative, you'll see the login screen with the GameNative logo and two login options.

<img src="Pictures%20for%20guide/Screenshot_20251102_114233_GameNative.jpg" alt="GameNative Login Screen" width="400">

*The GameNative login screen with QR Code and Credentials options*

#### Permission Requests
- On Android 13+, you'll be prompted to allow notifications
- When installing your first game, you'll be asked for storage permissions
- **Grant these permissions** for the best experience

#### Choose Your Login Method

**Option 1: QR Code Login (Recommended)**
1. Tap the **QR Code** tab
2. Open Steam on your desktop/laptop
3. Go to **Steam** → **Settings** → **Security**
4. Click **Use QR Code to sign in** or **Manage Steam Guard**
5. Scan the QR code displayed in GameNative with the Steam Mobile app or your desktop camera
6. Confirm the login on your other device

**Option 2: Username & Password Login**
1. Tap the **Credentials** tab
2. Enter your **Steam Username** (not email)
3. Enter your **Steam Password**
4. Check **Remember Me** if you want to stay logged in
5. Tap **Login**
6. If you have Steam Guard enabled, you'll be prompted for your 2FA code

#### Two-Factor Authentication (2FA)
If your account has Steam Guard:
- **Email Code:** Check your email for the code from Steam
- **Mobile Authenticator:** Open Steam Mobile App → Get code
- Enter the code in GameNative and tap **Submit**

#### First Connection
- GameNative will connect to Steam servers (may take 10-30 seconds)
- You'll see "Connecting to Steam..." while establishing connection
- Once connected, you'll see your game library

> **Tip:** The first boot may take a few minutes while GameNative sets up the emulation environment. Subsequent launches will be much faster!

---

## 🎮 Installing Your First Game

### Step 1: Browse Your Library
1. After login, you'll land on the **Library** tab
2. Scroll through your Steam games
3. Use the search icon to find specific games
4. Games you own will be displayed with their cover art


<img src="Pictures for guide/Screenshot_20251102_114358_GameNative.jpg" alt="Showing Library" width="400">

*Showing Library*

### Step 2: Select a Game
1. Tap on a game to view its details
2. You'll see:
   - Game name and cover art
   - Play button (if installed)
   - Install button (if not installed)
   - Storage requirements

<img src="Pictures%20for%20guide/Screenshot_20251102_114406_GameNative.jpg" alt="Game Not Installed" width="400">

*20 Minutes Till Dawn Not Installed*



### Step 3: Install the Game

#### Check Storage Requirements
Before installing, review:
- **Download Size:** Amount of data to download
- **Installed Size:** Space needed on your device
- **Available Space:** How much free space you currently have

#### Grant Storage Permission
On first installation:
1. Tap **Install**
2. Grant **Storage Permission** when prompted
3. This allows GameNative to create game folders

<img src="Pictures%20for%20guide/Screenshot_20251102_114411_Permission%20controller.jpg" alt="Storage Permission Request" width="400">

*Granting storage permission for game installation*

<img src="Pictures%20for%20guide/Screenshot_20251102_114430_GameNative.jpg" alt="Game Installed" width="400">

*20 Minutes Till Dawn Installed*

<img src="Pictures%20for%20guide/Screenshot_20251102_114436_GameNative.jpg" alt="Game First Run" width="400">

*Game First Run*

#### Choose Installation Options
1. Tap **Install**
2. Review the storage requirements dialog
3. Tap **Proceed** to confirm

<img src="Pictures%20for%20guide/Screenshot_20251102_114416_GameNative.jpg" alt="Storage Requirements Dialog" width="400">

*Reviewing storage requirements before installation*

#### Monitor Download Progress
- Download progress appears on the game's detail page in the Library
- You can see:
  - Download percentage and progress bar
  - Time remaining estimate
  - Current status (downloading, installing, etc.)

<img src="Pictures%20for%20guide/Screenshot_20251102_114729_GameNative.jpg" alt="Game Installed" width="400">

*Game successfully installed and ready to play*

> **Note:** Large games may take significant time. Downloads continue in the background, and you'll get a notification when complete.

#### DLC Installation
- If the game has DLC, GameNative will detect your owned DLC automatically
- DLC will be downloaded along with the base game
- Optional DLC can be managed in the game's settings after installation

---

## 🕹️ Playing Games

### Launching a Game

1. Go to **Library** tab
2. Tap the game you want to play
3. Tap **Play**
4. GameNative will:
   - Prepare the game container (first time only)
   - Sync cloud saves if enabled
   - Show a brief loading screen with tips
   - Launch the game

<img src="Pictures%20for%20guide/Screenshot_20251102_114729_GameNative.jpg" alt="Game Ready to Play" width="400">

*Game installed and ready to launch*

> **First Launch:** The first time you launch any game, expect a 2-5 minute setup as GameNative creates the Wine environment. A booting splash screen will show helpful tips during this time.

### In-Game Experience

#### Controls
- **Touch Input:** Works for point-and-click games
- **External Controllers:** Bluetooth/USB controllers are supported
  - XInput API enabled by default
  - Configure in Settings → Emulation → Container Config → Input tab

<img src="Pictures%20for%20guide/Screenshot_20251102_115201_GameNative.jpg" alt="In-Game Screenshot" width="600">

*Game running in GameNative*

#### Screen Orientation
- By default, landscape orientations are enabled
- Change in **Settings** → **Emulation** → **Allowed Orientations**
- Options: Portrait, Landscape, Reverse Portrait, Reverse Landscape

#### Performance Overlay
- Toggle FPS counter in game settings
- Located in the game's individual settings page

### Exiting a Game

- Use the **Back button** to exit the game
- GameNative will:
  - Close the game properly
  - Sync cloud saves (if enabled and online)
  - Return you to the library

---

## ⚙️ Configuration & Settings

### Accessing Settings
Tap the **Settings** icon (gear) in the top-right corner of the Library screen.

<img src="Pictures%20for%20guide/Screenshot_20251102_115220_GameNative.jpg" alt="Game Detail Options" width="400">

*Game detail page with options menu (three-dot menu)*

### Settings Overview

#### Emulation Settings
Located in **Settings** → **Emulation**:

- **Allowed Orientations:** Choose which screen rotations are permitted
- **Modify Default Config:** Configure default settings for all games
  - Screen size
  - Graphics driver
  - Wine version
  - Audio settings
  - Input options
- **Box64 Presets:** Advanced CPU emulation presets for performance tuning
- **Driver Manager:** Install and manage graphics drivers (Adreno, Snapdragon, etc.)
- **Contents Manager:** Manage Wine versions and FEXCore versions

#### Interface Settings
Located in **Settings** → **Interface**:

- **Theme:** Choose between Light, Dark, or System theme
- **Color Palette:** Customize the app's color scheme
- **Open Web Links Externally:** Choose whether links open in external browser
- **Download Region:** Select your Steam download region (automatic by default)

#### Per-Game Configuration
Each game has individual settings accessible from its detail page:

1. Go to **Library** → Select a game
2. Tap the **gear icon** (settings)
3. Configure tabs:
   - **General:** Screen size, Wine version, language
   - **Graphics:** Driver, DXVK/WineD3D, video memory, rendering options
   - **Advanced:** FEXCore CPU emulation settings (TSO mode, x87 mode, multiblock)
   - **Input:** Controller settings (XInput, SDL API)
   - **Audio:** Audio driver, performance mode, latency, volume
   - **Environment Variables:** Add custom variables
   - Drives:** Manage virtual drives (C: drive is the game directory)
   - **Startup:** Processor affinity, startup programs

<img src="Pictures%20for%20guide/Screenshot_20251102_115226_GameNative.jpg" alt="Container Config General Tab" width="400">

*Container configuration - General tab showing executable path and arguments*

<img src="Pictures%20for%20guide/Screenshot_20251102_115233_GameNative.jpg" alt="Container Config Graphics Tab" width="400">

*Container configuration - Graphics tab with driver and Vulkan settings*

> **Tip:** Most games work well with default settings. Only adjust these if you encounter issues or want to optimize performance.

### Cloud Save Sync

GameNative automatically syncs your Steam Cloud saves:

- **Before launching:** Downloads latest saves from Steam Cloud
- **After closing:** Uploads your progress to Steam Cloud
- Works seamlessly if you also play on PC
- Requires internet connection for sync

<img src="Pictures%20for%20guide/Screenshot_20251102_115426_GameNative.jpg" alt="Cloud Sync" width="400">

*Cloud save synchronization dialog*

**Conflict Resolution:**
- If local and cloud saves conflict, you'll be prompted to choose
- This can happen if you played offline on multiple devices

---

## 🔧 Troubleshooting

### Common Issues

#### "No connection to Steam"
**Symptoms:** Login screen shows connection error
**Solutions:**
1. Check your internet connection
2. Try switching between Wi-Fi and mobile data
3. Tap **Retry Connection**
4. If Steam servers are down, wait and try again later
5. You can tap **Continue Offline** to access already-installed games

#### Game Won't Launch
**Symptoms:** Game crashes immediately or shows black screen
**Solutions:**
1. Check the game files in **Open Container** (in game settings)
2. Install DirectX/Visual C++ redistributables:
   - Open game container → Navigate to `A:\_CommonRedist`
   - Run installers for DirectX, vcredist, .NET Framework, etc.
3. Try different Wine versions in game settings
4. Switch graphics drivers (Settings → Emulation → Driver Manager)
5. Adjust Box64 presets for CPU compatibility

#### Performance Issues
**Symptoms:** Low FPS, stuttering, lag
**Solutions:**
1. **Close background apps** on your device
2. **Lower game graphics settings** within the game
3. Try a different **Graphics Driver**:
   - Settings → Emulation → Driver Manager
   - Adreno/Snapdragon drivers may perform better on Qualcomm devices
4. Adjust **Box64 Presets**:
   - Settings → Emulation → Box64 Presets
   - Try different TSO modes (Fast/Accurate) and multiblock settings
5. **Reduce screen resolution** in game's Container Config
6. Ensure **Enable XInput API** is on for controller games

#### Download Stuck or Slow
**Symptoms:** Downloads not progressing or very slow speeds
**Solutions:**
1. Check your internet connection speed
2. Switch to Wi-Fi if on mobile data
3. Try changing **Download Region**:
   - Settings → Interface → Download Region
   - Choose a closer or less congested region
4. Pause and resume the download
5. Check available storage space

#### Storage Permission Denied
**Symptoms:** Can't install games or open containers
**Solutions:**
1. Go to Android **Settings** → **Apps** → **GameNative** → **Permissions**
2. Grant **Storage** permission
3. On Android 11+, ensure **All files access** is granted if needed

#### Cloud Save Conflicts
**Symptoms:** Prompted to choose between local and cloud saves
**Explanation:** This happens when you've played on multiple devices offline
**Solutions:**
1. **Choose Cloud** if you want the most recent online save
2. **Choose Local** if your device has more recent progress
3. To avoid conflicts, always ensure internet connection when playing if you use multiple devices

#### Game Crashes to Black Screen
**Solutions:**
1. Some games require specific Windows components:
   - Open Container → `A:\_CommonRedist`
   - Install DirectX, .NET Framework, Visual C++ runtimes
2. Check Wine debug logs:
   - Settings → Debug → View Wine Logs
3. Try a different Wine version in game settings

---

## 🆘 Getting Help

### Community Support

**Discord Server**
- Join the GameNative community: [discord.gg/2hKv4VfZfE](https://discord.gg/2hKv4VfZfE)
- Get help from other users
- Share your game compatibility experiences
- Report bugs and suggest features

### Debug Information

When reporting issues, include:
1. **Device Info:** Brand, model, Android version, RAM
2. **Game Name:** Which game is having issues
3. **Error Messages:** Screenshots or exact text
4. **Steps to Reproduce:** What you did before the issue occurred
5. **Wine Logs:** 
   - Settings → Debug → View Wine Logs
   - Copy and share relevant log sections

### View Logs
Access detailed logs for troubleshooting:
1. **Settings** → **Debug**
2. **View Wine Logs:** See emulation layer logs
3. **View FEXCore Logs:** See CPU emulation logs
4. Long-press debug options to export logs

---

## 💡 Tips & Best Practices

### Storage Management
- **Use External Storage:** If your device supports it, move games to SD card or USB OTG
  - Settings → Emulation → Modify Default Config → Storage
- **Regularly clean up:** Uninstall games you're not playing to free up space

### Battery & Performance
- GameNative keeps your screen on while games are running
- Close the app when not in use to save battery
- Some games are CPU/GPU intensive and will drain battery faster

### Game Compatibility
- **Not all Windows games work perfectly** on Android + Wine + FEXCore
- Start with **lighter, older games** to test compatibility
- **2D games** and **indie titles** generally work better than AAA 3D games
- Check the Discord community for compatibility reports

### Offline Mode
- You can play installed games offline after initial setup
- Cloud saves won't sync in offline mode
- Login at least once online to access your library

### Updates
- Check the [Releases page](https://github.com/gabrielnovais/GameNative/releases) periodically for updates
- New versions may include:
  - Better game compatibility
  - Performance improvements
  - Bug fixes
  - New features

---

## 🎯 Quick Reference

### Navigation
- **Library:** Browse and launch your Steam games
- **Settings:** Configure app and game settings (gear icon in top bar)

### Keyboard Shortcuts
- **Back Button:** Exit game or go back in menus
- **Volume Buttons:** (if mapped in game settings)

### Default Paths
Games are installed to one of these locations:
- **Internal Storage:** `/data/data/app.gamenative/files/Steam/`
- **External Storage:** `/sdcard/GameNative/Steam/` (if configured)

---

## 📄 Additional Resources

- **Privacy Policy:** [GameNative Privacy Policy](https://github.com/gabrielnovais/GameNative/blob/main/PrivacyPolicy/README.md)
- **Source Code:** [GitHub Repository](https://github.com/gabrielnovais/GameNative)
- **Building from Source:** See the main [README.md](README.md) for build instructions
- **Support Development:** [Ko-fi](https://ko-fi.com/gabrielnovais)

---

## ❓ FAQ

**Q: Do I need a Steam Deck or gaming PC to use GameNative?**  
A: No! You only need a Steam account with games in your library. GameNative runs the Windows versions of your games directly on Android.

**Q: Can I play online multiplayer games?**  
A: Some online games may work, but anti-cheat systems often prevent running games on Wine/emulation. Single-player and local co-op games work best.

**Q: Will this drain my battery quickly?**  
A: Running Windows games through emulation is intensive. Expect higher battery usage, especially with 3D games. Play while charging for extended sessions.

**Q: Can I use a keyboard and mouse?**  
A: Yes! Bluetooth and USB OTG keyboards/mice should work. Connect them before launching the game.

**Q: How much storage do I need?**  
A: Depends on the games you install. Small indie games may be under 1GB, while AAA titles can be 50GB+. Check each game's requirements before installing.

**Q: Can I transfer my saves between GameNative and my PC?**  
A: Yes! If the game supports Steam Cloud saves, they'll sync automatically. Make sure you're online when launching and closing games.

**Q: Why does the first launch take so long?**  
A: The first time you launch any game, GameNative needs to set up the Wine environment, which can take 2-5 minutes. Subsequent launches are much faster.

**Q: What graphics drivers should I use?**  
A: Try the default driver first. If you have a Qualcomm Snapdragon device, the Adreno or Snapdragon 8 Elite drivers may improve performance significantly.

**Q: Is GameNative legal?**  
A: Yes! You're running your own legally purchased games using open-source emulation software (Wine/FEXCore). However, you must own the games on Steam.

---

**Welcome to GameNative! Enjoy your Steam library on the go! 🎮**

For the latest updates and announcements, join our Discord community or follow the GitHub repository.

---

*Last Updated: January 2025*  
*GameNative Version: Latest*  
*License: GPL 3.0*

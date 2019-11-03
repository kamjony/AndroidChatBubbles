# AndroidChatBubbles

Chat Heads/Notification Bubbles

This project demonstrates a simple chat head/notification bubble which also allows a user to drag them across the screen.

In this sample project demonstrates how we can create simple chat heads and allow user to drag them across the screen. So that user can adjust the position of the chat head.

Steps:
Create chat head layout.

Create service for chat heads and add the chat head layout to the display window.

Override the OnTouch(), to respond the drag and move event.

Add android.permission.SYSTEM_ALERT_WINDOW permission to the AndroidManifest.xml and check if the permission available in runtime before starting chat head service.

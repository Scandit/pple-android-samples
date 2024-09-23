# Java Simple Sample

Written in Java, rudimentary sample that shows how to quickly and easily set up Price Checking with PPLE SDK.
It is a single Activity (and without Fragments) application, that demonstrates how to:
1. authenticate with ShelfView credentials,
2. fetch the stores, product catalog and the config,
3. add the `CaptureView` UI component,
4. begin price checking.

The app applies the most typical settings you would expect in the price checking flow, including:
* a rectangular viewfinder placed in the middle of the screen,
* a restriction of the active scanning area to part of the screen within that viewfinder,
* AR overlays floating on top of the barcode belonging to the price label that has been detected within the active scan area.

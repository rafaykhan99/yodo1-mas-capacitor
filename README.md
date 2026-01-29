# @yodo1/mas-capacitor

A Capacitor Plugin for Yodo1 MAS

## Install

```bash
npm install @yodo1/mas-capacitor
npx cap sync
```

## API

<docgen-index>

* [`initialize(...)`](#initialize)
* [`loadAd(...)`](#loadad)
* [`showAd(...)`](#showad)
* [`isAdLoaded(...)`](#isadloaded)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialize(...)

```typescript
initialize(options: InitializeOptions) => Promise<InitializeResult>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#initializeoptions">InitializeOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#initializeresult">InitializeResult</a>&gt;</code>

--------------------


### loadAd(...)

```typescript
loadAd(options: LoadAdOptions) => Promise<ActionResult>
```

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#loadadoptions">LoadAdOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#actionresult">ActionResult</a>&gt;</code>

--------------------


### showAd(...)

```typescript
showAd(options: ShowAdOptions) => Promise<ActionResult>
```

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#showadoptions">ShowAdOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#actionresult">ActionResult</a>&gt;</code>

--------------------


### isAdLoaded(...)

```typescript
isAdLoaded(options: IsAdLoadedOptions) => Promise<IsAdLoadedResult>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#isadloadedoptions">IsAdLoadedOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#isadloadedresult">IsAdLoadedResult</a>&gt;</code>

--------------------


### Interfaces


#### InitializeResult

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`initialized`** | <code>boolean</code> |
| **`errorCode`**   | <code>string</code>  |
| **`message`**     | <code>string</code>  |


#### InitializeOptions

| Prop                      | Type                 |
| ------------------------- | -------------------- |
| **`appKey`**              | <code>string</code>  |
| **`coppa`**               | <code>boolean</code> |
| **`gdpr`**                | <code>boolean</code> |
| **`ccpa`**                | <code>boolean</code> |
| **`autoDelayIfLoadFail`** | <code>boolean</code> |


#### ActionResult

| Prop              | Type                                      |
| ----------------- | ----------------------------------------- |
| **`ok`**          | <code>boolean</code>                      |
| **`action`**      | <code>'loadAd' \| 'showAd'</code>         |
| **`adType`**      | <code><a href="#adtype">AdType</a></code> |
| **`placementId`** | <code>string</code>                       |


#### LoadAdOptions

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`adType`** | <code><a href="#adtype">AdType</a></code> |


#### ShowAdOptions

| Prop              | Type                                      |
| ----------------- | ----------------------------------------- |
| **`adType`**      | <code><a href="#adtype">AdType</a></code> |
| **`placementId`** | <code>string</code>                       |


#### IsAdLoadedResult

| Prop           | Type                                      |
| -------------- | ----------------------------------------- |
| **`isLoaded`** | <code>boolean</code>                      |
| **`adType`**   | <code><a href="#adtype">AdType</a></code> |


#### IsAdLoadedOptions

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`adType`** | <code><a href="#adtype">AdType</a></code> |


### Type Aliases


#### AdType

<code>'appopen' | 'interstitial' | 'rewarded'</code>

</docgen-api>

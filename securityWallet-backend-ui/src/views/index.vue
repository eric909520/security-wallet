<template>
  <div class="dashboard-editor-container">
    <!-- 首页 -->
    <div v-if="false"></div>

    <div v-else class="welcome">
      <h2 class="titel">欢迎来到后台管理系统</h2>
    </div>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      isShow: false
    };
  },
  created() {},
  methods: {}
};
</script>

<style lang="scss" scoped>
.welcome {
  width: 100%;
  height: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("../assets/image/shouye.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  .titel {
    font-size: 66px;
    color: #fff;
    text-shadow: rgb(94, 94, 94) 1px 2px 2px;
  }
}

.dashboard-editor-container {
  padding: 32px;
  position: relative;
  background-color: #fff;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>

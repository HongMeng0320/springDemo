import { App } from 'vue';
import { 
  ElButton, 
  ElInput, 
  ElForm, 
  ElFormItem, 
  ElTable, 
  ElTableColumn,
  ElPagination,
  ElMenu,
  ElMenuItem,
  ElSubMenu,
  ElDropdown,
  ElDropdownMenu,
  ElDropdownItem,
  ElCard,
  ElDialog,
  ElSelect,
  ElOption,
  ElDatePicker,
  ElBreadcrumb,
  ElBreadcrumbItem,
  ElTabs,
  ElTabPane,
  ElAlert,
  ElTag,
  ElDivider,
  ElConfigProvider
} from 'element-plus';

// 全局消息方法
import { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus';

// 组件列表
const components = [
  ElButton,
  ElInput,
  ElForm,
  ElFormItem,
  ElTable,
  ElTableColumn,
  ElPagination,
  ElMenu,
  ElMenuItem,
  ElSubMenu,
  ElDropdown,
  ElDropdownMenu,
  ElDropdownItem,
  ElCard,
  ElDialog,
  ElSelect,
  ElOption,
  ElDatePicker,
  ElBreadcrumb,
  ElBreadcrumbItem,
  ElTabs,
  ElTabPane,
  ElAlert,
  ElTag,
  ElDivider,
  ElConfigProvider
];

export default {
  install(app: App) {
    // 注册组件
    components.forEach(component => {
      // 确保组件名称存在
      if (component.name) {
        app.component(component.name, component);
      }
    });
    
    // 挂载全局方法
    app.config.globalProperties.$message = ElMessage;
    app.config.globalProperties.$msgbox = ElMessageBox;
    app.config.globalProperties.$alert = ElMessageBox.alert;
    app.config.globalProperties.$confirm = ElMessageBox.confirm;
    app.config.globalProperties.$prompt = ElMessageBox.prompt;
    app.config.globalProperties.$notify = ElNotification;
    app.config.globalProperties.$loading = ElLoading.service;
  }
}; 